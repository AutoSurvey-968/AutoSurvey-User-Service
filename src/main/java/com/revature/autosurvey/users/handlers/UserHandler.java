package com.revature.autosurvey.users.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.firebase.auth.FirebaseAuthException;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.PasswordChangeRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.errors.AuthorizationException;
import com.revature.autosurvey.users.errors.NotFoundException;
import com.revature.autosurvey.users.errors.UserAlreadyExistsException;
import com.revature.autosurvey.users.security.FirebaseUtil;
import com.revature.autosurvey.users.security.SecurityContextRepository;
import com.revature.autosurvey.users.services.UserService;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {

	private UserService userService;
	private FirebaseUtil firebaseUtil;
	private Logger log = LoggerFactory.getLogger(UserHandler.class);

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
				.body(userService.addUser(user).switchIfEmpty(Mono.error(new UserAlreadyExistsException())), User.class));

	}

	public Mono<ServerResponse> login(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(req.bodyToMono(LoginRequest.class)
						.flatMap(login -> userService.findByUsername(login.getEmail())
								.switchIfEmpty(Mono.error(new NotFoundException()))
								.flatMap(foundUser -> userService.login(foundUser, login).flatMap(loggedUser -> {
									try {
										req.exchange().getResponse()
												.addCookie(ResponseCookie
														.from(SecurityContextRepository.COOKIE_KEY,
																firebaseUtil.generateToken(loggedUser))
														.path("/").httpOnly(true).build());
									} catch (FirebaseAuthException fae) {
										return Mono.error(fae);
									}
									return Mono.just(loggedUser);
								}))), User.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUserById(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUserById(req.pathVariable("id")), User.class);
	}

	public  Mono<ServerResponse> getUserEmail(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUserById(req.pathVariable("id")), User.class);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> updateUser(ServerRequest req) {
		return null;
	}
	
	@PreAuthorize("hasRole('USER')")
	public Mono<ServerResponse> updatePassword(ServerRequest req) {
		if (req.cookies().get("token") == null) {
			return Mono.error(new AuthorizationException());
		}
		req.bodyToMono(PasswordChangeRequest.class).doOnNext(pcr -> userService.updatePassword(pcr)).subscribe();
		return ServerResponse.noContent().build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> deleteUser(ServerRequest req) {
		return null;

	}
}
