package com.revature.autosurvey.users.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
	@Primary
	RouterFunction<ServerResponse> routes() {
		return RouterFunctions
				.route(RequestPredicates.GET("/users"), uc::getUsers)
				.andRoute(RequestPredicates.POST("/users"), uc::addUser)
				.andRoute(RequestPredicates.PUT("/users"), uc::login);
	}
}
