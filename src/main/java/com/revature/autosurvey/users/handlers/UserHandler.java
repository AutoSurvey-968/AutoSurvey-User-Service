package com.revature.autosurvey.users.handlers;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
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
		return req.bodyToMono(User.class)
				.flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
						userService.addUser(user).switchIfEmpty(Mono.error(new UserAlreadyExistsException())),
						User.class));

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
								}))),
						User.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUserById(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUserById(req.pathVariable("id")), User.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> getUserByEmail(ServerRequest req) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUserByEmail(req.queryParam("email").get()), User.class);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Mono<ServerResponse> updateUser(ServerRequest req) {
		return ServerResponse
				.ok().contentType(
						MediaType.APPLICATION_JSON)
				.body(req.bodyToMono(JsonObject.class).flatMap(jUser -> req.bodyToMono(User.class)
						.flatMap(user -> {
							user.setId(Integer.parseInt(req.pathVariable("id")));
							return userService.updateUser(user, jUser.keySet());
						})), User.class);
	}

	@PreAuthorize("hasRole('USER')")
	public Mono<ServerResponse> updatePassword(ServerRequest req) {
		if (req.cookies().getFirst(SecurityContextRepository.COOKIE_KEY) == null) {
			return Mono.error(new AuthorizationException());
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
		return null;

	}
}
