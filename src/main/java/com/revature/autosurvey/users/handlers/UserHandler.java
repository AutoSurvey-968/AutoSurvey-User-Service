package com.revature.autosurvey.users.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.firebase.auth.FirebaseAuthException;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.PasswordChangeRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.UserWithToken;
import com.revature.autosurvey.users.errors.AuthorizationError;
import com.revature.autosurvey.users.errors.UserAlreadyExistsError;
import com.revature.autosurvey.users.security.FirebaseUtil;
import com.revature.autosurvey.users.security.SecurityContextRepository;
import com.revature.autosurvey.users.services.UserService;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	private UserService userService;
	private FirebaseUtil firebaseUtil;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setFirebaseUtil(FirebaseUtil firebaseUtil) {
		this.firebaseUtil = firebaseUtil;
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUsers(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllUsers(), User.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getIdTable(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getIdTable(), Id.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> addUser(ServerRequest req) {
		return req.bodyToMono(User.class).flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.addUser(user).switchIfEmpty(Mono.error(new UserAlreadyExistsError())), User.class));

	}

	public Mono<ServerResponse> login(ServerRequest req) {
		//Get Login Request
		Mono<LoginRequest> loginReq = req.bodyToMono(LoginRequest.class);
		//Get the user to put in the body
		Mono<UserWithToken> user = loginReq.flatMap(login->{
			// get the user details
			Mono<UserDetails> uDetails = userService.findByUsername(login.getEmail());
//					.switchIfEmpty(Mono.error(new NotFoundError())); we throw errors now
			return uDetails
					//find the User
					.flatMap(foundUser -> userService.login(foundUser, login))
					//logs in
					.flatMap(loggedUser -> {
						String token = null;
						try {
							token = firebaseUtil.generateToken(loggedUser);
							ResponseCookie cookie = ResponseCookie
									.from(SecurityContextRepository.COOKIE_KEY, token)
									.path("/").httpOnly(true).build(); 
							//the problem
							req.exchange().getResponse().addCookie(cookie);
						} catch (FirebaseAuthException fae) {
							return Mono.error(fae);//when something happens in firebase
						}
						return Mono.just(new UserWithToken(loggedUser, token));
			});
			
			
		});
		
		return user.flatMap(
				bodyUser -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(Mono.just(bodyUser), User.class))
				.onErrorResume(Mono::error);//returns this if theres an error
	}
	
	public Mono<ServerResponse> logout(ServerRequest req) {
		req.exchange().getResponse().addCookie(ResponseCookie.from(SecurityContextRepository.COOKIE_KEY, "").path("/")
				.httpOnly(true).maxAge(0).build());
		return ServerResponse.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUserById(ServerRequest req) {
		return userService.getUserById(req.pathVariable("id"))
				.flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(u), User.class));
				
	}


	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUserByEmail(ServerRequest req) {
		return userService.getUserByEmail(req.queryParam("email").get())
				 .flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(u), User.class));
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> updateUser(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(req.bodyToMono(User.class).flatMap(user -> {
					user.setId(Integer.parseInt(req.pathVariable("id")));
					return userService.updateUser(user);
				}), User.class);
	}

	@PreAuthorize("hasRole('USER')")
	public Mono<ServerResponse> updatePassword(ServerRequest req) {
		if (req.cookies().getFirst(SecurityContextRepository.COOKIE_KEY) == null) {
			return Mono.error(new AuthorizationError());
		}
		return ServerResponse
				.status(204).body(
						req.bodyToMono(PasswordChangeRequest.class)
								.flatMap(pcr -> firebaseUtil
										.getDetailsFromCustomToken(
												req.cookies().getFirst(SecurityContextRepository.COOKIE_KEY).getValue())
										.flatMap(decodedToken -> userService.updatePassword(pcr, decodedToken))),
						Object.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> deleteUser(ServerRequest req) {
		return userService.getUserById(req.pathVariable("id"))
				.flatMap(u -> ServerResponse.status(204)
						.body(userService.deleteUser(Integer.parseInt(req.pathVariable("id"))), Object.class));

	}
}
