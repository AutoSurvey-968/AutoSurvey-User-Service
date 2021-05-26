package com.revature.autosurvey.users.routes;

import java.util.NoSuchElementException;

import org.apache.http.HttpStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;

import com.google.firebase.auth.FirebaseAuthException;
import com.revature.autosurvey.users.errors.AuthorizationError;
import com.revature.autosurvey.users.errors.NotFoundError;
import com.revature.autosurvey.users.errors.UserAlreadyExistsError;
import com.revature.autosurvey.users.handlers.UserHandler;



@Configuration
public class UserRoutes {
	
	@Bean
	RouterFunction<ServerResponse> routes(UserHandler uh) {
		return RouterFunctions.route().path("/",
				builder -> builder
				.GET("id", RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::getIdTable)
				.GET("{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::getUserById)
				.PUT("{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::updateUser)
				.DELETE("{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::deleteUser)
				.PUT("{id}/password", RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::updatePassword)
				.GET(RequestPredicates.queryParam("email", t -> true), uh::getUserByEmail)
				.GET(RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::getUsers)
				.POST(RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::addUser)
				.PUT(RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::login))
				.DELETE(RequestPredicates.accept(MediaType.APPLICATION_JSON), uh::logout)
				.build();
	}
	
	@Bean
	WebFilter exceptionToErrorCode() {
		return (exchange, next) -> next.filter(exchange)
				.onErrorResume(NotFoundError.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_NOT_FOUND);
					return response.setComplete();
				})
				.onErrorResume(UserAlreadyExistsError.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_CONFLICT);
					return response.setComplete();
				})
				.onErrorResume(AuthorizationError.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_FORBIDDEN);
					return response.setComplete();
				})
				.onErrorResume(FirebaseAuthException.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_FORBIDDEN);
					return response.setComplete();
				})
				.onErrorResume(NoSuchElementException.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_BAD_REQUEST);
					return response.setComplete();
				})
				.onErrorResume(NumberFormatException.class, e -> {
					ServerHttpResponse response = exchange.getResponse();
					response.setRawStatusCode(HttpStatus.SC_BAD_REQUEST);
					return response.setComplete();
				});
	}
}
