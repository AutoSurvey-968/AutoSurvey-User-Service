package com.revature.autosurvey.users.services.test;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@TestConfiguration
	static class Config{
		
		@Bean
		public UserService getUserService(UserRepository userRepository){
			UserServiceImp usi = new UserServiceImp();
			usi.setUserRepository(userRepository);
			return usi;
		}
		
		@Bean
		public UserRepository getUserRepo() {
			return Mockito.mock(UserRepository.class);
		}
	}
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserServiceImp userService;
	
	@Test
	void testAddUserAddsUser() {
		User user = new User();
		user.setEmail("test@test.com");
		Mockito.when(userRepository.existsById(user.getEmail()))
			.thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(userRepository.insert(user)).thenReturn(Mono.just(user));
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectNext(user).verifyComplete();		
	}
	
	@Test
	void testAddUserFailReturnEmpty() {
		User user = new User();
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).verifyComplete();
	}
	
	@Test
	void testGetAllUsers() {
		User u1 = new User();
		User u2 = new User();
		User u3 = new User();

		Mockito.when(userRepository.findAll()).thenReturn(Flux.just(u1, u2, u3));
		Flux<User> result = userService.getAllUsers();
		StepVerifier.create(result).expectNext(u1, u2, u3).verifyComplete();
	}
	
	@Test
	void testUpdateUser() {
		User u1 = new User();
		u1.setEmail("text@text.com");
		Mockito.when(userRepository.findById(u1.getEmail()))
		.thenReturn(Mono.just(u1));
		Mono<User> result = userService.getUserById("text@text.com").doOnNext(u -> {
			u.setEmail("true");
		}).map(c -> c);
		StepVerifier.create(result).expectNextMatches(u -> u.getEmail().equals("true"))
		.verifyComplete();
		
	}
}
