package com.revature.autosurvey.users.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.firebase.auth.FirebaseAuthException;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.errors.NotFoundError;
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

	public Mono<ServerResponse> getUsers(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllUsers(), User.class);
	}

	public Mono<ServerResponse> addUser(ServerRequest req) {
		return req.bodyToMono(User.class)
				.flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(userService.addUser(user), User.class))
				.doOnError(e -> ServerResponse.badRequest().body(e.getMessage(), String.class));
	}

	public Mono<ServerResponse> login(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(req.bodyToMono(LoginRequest.class).flatMap(login -> userService.findByUsername(login.getEmail())
						.switchIfEmpty(Mono.error(new NotFoundError())).flatMap(foundUser -> {
							if (foundUser.getPassword().equals(login.getPassword())) {
								try {
									req.cookies().add(SecurityContextRepository.COOKIE_KEY, ResponseCookie
											.from(SecurityContextRepository.COOKIE_KEY, firebaseUtil.generateToken((User) foundUser))
											.path("/").httpOnly(true).build());
								} catch (FirebaseAuthException fae) {
									return Mono.error(fae);
								}
								return Mono.just(foundUser).cast(User.class);
							} else {
								return Mono.empty();
							}
						})).switchIfEmpty(Mono.error(new NotFoundError())), User.class);
	}

	public Mono<ServerResponse> getUserById(ServerRequest req) {
		return null;
	}

	public void getUserEmail() {

	}

	public Mono<ServerResponse> updateUser(ServerRequest req) {
		return null;
	}

	public Mono<ServerResponse> deleteUser(ServerRequest req) {
		return null;

	}
}
