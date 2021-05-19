package com.revature.autosurvey.users.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import com.revature.autosurvey.users.controllers.UserController;



@Configuration
public class UserRoutes {
	@Autowired
	UserController uc;
	
	@Bean
	RouterFunction<ServerResponse> routes() {
		return RouterFunctions.route().path("/users",
				builder -> builder
				.GET("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uc::getUserById)
				.DELETE("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uc::deleteUser)
				.PUT("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), uc::updateUser)
				.GET(uc::getUsers)
				.POST(uc::addUser)
				.PUT(uc::login))
				.build();
	}
}
