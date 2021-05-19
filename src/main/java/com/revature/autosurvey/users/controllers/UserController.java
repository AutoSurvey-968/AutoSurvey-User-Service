package com.revature.autosurvey.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.services.UserService;

import reactor.core.publisher.Mono;

@Component
public class UserController {
	private UserService userService;
	private User emptyUser;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Mono<ServerResponse> getUsers(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getAllUsers(), User.class);
	}

//	@PostMapping
	public Mono<ServerResponse> addUser(ServerRequest req) {
		return req.bodyToMono(User.class)
				.flatMap(user -> userService.addUser(user)
						.flatMap(u -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(u, User.class))
						.doOnError(e -> Mono.just(e.getMessage()))
						.flatMap(s -> ServerResponse.status(409).contentType(MediaType.TEXT_PLAIN).bodyValue(s)));
	}

//	@PutMapping
	public Mono<ServerResponse> login(ServerRequest req) {
		return null;
	}

//	@GetMapping("/{id}")
	public Mono<ServerResponse> getUserById(ServerRequest req) {
		return null;
	}

//	@GetMapping
	public void getUserEmail() {

	}

//	@PutMapping("/{id}")
	public Mono<ServerResponse> updateUser(ServerRequest req) {
		return null;
	}

//	@DeleteMapping("/{id}")
	public Mono<ServerResponse> deleteUser(ServerRequest req) {
		return null;

	}
}
