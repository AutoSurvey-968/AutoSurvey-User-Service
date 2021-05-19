package com.revature.autosurvey.users.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.services.UserService;



@ExtendWith(SpringExtension.class)
public class UserControllerTest {
	@TestConfiguration
	static class Configuration {
		
		@Bean
		public UserController getUserController(UserService userService, User user) {
			UserController userController = new UserController();
			userController.setUserService(userService);
			return userController;
		}
	}

}
