package com.revature.autosurvey.users.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.data.UserRepository;
import com.revature.autosurvey.services.UserService;
import com.revature.autosurvey.services.UserServiceImp;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@TestConfiguration
	static class Config{
		
		@Bean
		public UserService getUserService(UserRepository userRepo){

			UserServiceImp usi = new UserServiceImp();
			usi.UserRepo();
			return usi;
		}
		
		@Bean
		public UserRepository getUserRepo() {
			return Mockito.mock(UserRepository.class);
		}
	}
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Test
	void UserServiceReturnsNull() {
		User user = new User();
		
		assertThat(userService.addUser(user)).isNull();
		assertThat(userService.deleteUser("test")).isNull();
		assertThat(userService.getAllUsers()).isNull();
		assertThat(userService.getUserById("test")).isNull();
		assertThat(userService.getUserByEmail("test")).isNull();
		assertThat(userService.updateUser(user)).isNull();
	}
}
