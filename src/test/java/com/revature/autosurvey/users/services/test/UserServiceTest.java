package com.revature.autosurvey.users.services.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.services.UserService;
import com.revature.autosurvey.users.services.UserServiceImp;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@TestConfiguration
	static class Config{
		
		@Bean
		public UserService getUserService(UserRepository userRepository){
			UserServiceImp usi = new UserServiceImp();
			usi.setUserRepo(userRepository);
			return usi;
		}
		
		@Bean
		public UserRepository getService() {
			return Mockito.mock(UserRepository.class);
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
	void userServiceReturnsNull() {
		User user = new User();
		
		assertThat(userService.addUser(user)).isNull();
		assertThat(userService.deleteUser(user)).isNull();
		assertThat(userService.getAllUsers()).isNull();
		assertThat(userService.getUserById("test")).isNull();
		assertThat(userService.getUserByEmail("test")).isNull();
		assertThat(userService.updateUser(user)).isNull();
	}
	
	
	@Test
	void deleteUser() {
		User u =new User();
		u.setEmail("a@a.com");
		
		String email = u.getEmail();
		
		when(userRepo.delete(u)).thenReturn(Mono.empty());
		StepVerifier.create(null);
		assertThat(userService.deleteUser(u)).isNull();
	}
}
