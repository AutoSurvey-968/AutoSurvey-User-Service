package com.revature.autosurvey.users.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.security.FirebaseUtil;
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
				.flatMap(user -> userService.addUser(user)
						.flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(u, User.class))
						.doOnError(e -> Mono.just(e.getMessage()))
						.flatMap(s -> ServerResponse.status(409).contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
	}


	public Mono<ServerResponse> login(ServerRequest req) {
		return req.bodyToMono(User.class)
				.flatMap(user -> userService.findByUsername(user.getEmail())
						.flatMap(u -> userService.login(u, user)
								.flatMap(foundUser -> ServerResponse.ok()
										.contentType(MediaType.APPLICATION_JSON)
										.body(foundUser, User.class)))
								.doOnError(e -> Mono.just(e.getMessage()))
								.flatMap(s ->  ServerResponse
										.status(404)
										.contentType(MediaType.TEXT_PLAIN)
										.bodyValue(s))
						.doOnError(e -> Mono.just(e.getMessage()))
						.flatMap(s ->  ServerResponse
								.status(404)
								.contentType(MediaType.TEXT_PLAIN)
								.bodyValue(s)));
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
