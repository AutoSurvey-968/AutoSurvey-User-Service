package com.revature.autosurvey.users.services.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.Id.Name;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.NotFoundError;
import com.revature.autosurvey.users.services.UserService;
import com.revature.autosurvey.users.services.UserServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@TestConfiguration
	static class Config {

		@Bean
		public UserService getUserService(UserRepository userRepository, PasswordEncoder encoder, IdRepository ir) {
			UserServiceImpl usi = new UserServiceImpl();
			usi.setIdRepository(ir);
			usi.setUserRepo(userRepository);
			usi.setPasswordEncoder(encoder);
			return usi;
		}

		@Bean
		public UserRepository getUserRepo() {
			return Mockito.mock(UserRepository.class);
		}
		
		@Bean
		public PasswordEncoder getEncoder() {
			return Mockito.mock(PasswordEncoder.class);
		}
		
		@Bean
		public IdRepository getIdRepo() {
			return Mockito.mock(IdRepository.class);
		}
	}
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IdRepository idRepository;

	@Autowired
	UserService userService;

	@Test
	void TestGetAllUsersreturnsFlux() {
		User u = new User();
		u.setEmail("a");
		u.setPassword("c");
		User u2 = new User();
		u2.setEmail("b");
		u2.setPassword("d");
		User[] uArr = { u, u2 };
		Flux<User> uFlux = Flux.fromArray(uArr);

		Mockito.when(userRepository.findAll()).thenReturn(uFlux);
		Flux<User> resultFlux = userService.getAllUsers();

		StepVerifier.create(resultFlux).expectNext(u).expectNext(u2).verifyComplete();
	}

	@Test
	void testDeleteUserReturnsEmpty() {
		User u = new User();
		u.setId(1);
		u.setEmail("a@a.com");
		u.setAuthorities(new ArrayList<>());
		
		when(userRepository.findById(u.getId())).thenReturn(Mono.just(u));
		when(userRepository.deleteById(1)).thenReturn(Mono.empty());
		when(userRepository.existsById(1)).thenReturn(Mono.just(true));
		Mono<User> result = userService.deleteUser(1);
		StepVerifier.create(result).expectComplete().verify();
	}

	@Test
	void testDeleteUserThrowsErrorIfnoUser() {
		Mono<User> noOne = Mono.empty();
		when(userRepository.existsById(1)).thenReturn(Mono.just(false));
		Mono<User> result = userService.deleteUser(1);
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectError(NotFoundError.class);
	}

	@Test
	void testAddUserAddsUser() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("a");
		user.setId(0);
		User encoded = user;
		encoded.setPassword("b");
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(idRepository.findById(Name.USER)).thenReturn(Mono.just(new Id()));
		Mockito.when(idRepository.save(Mockito.any())).thenReturn(Mono.just(new Id()));
		Mockito.when(userRepository.insert(user)).thenReturn(Mono.just(user));
		Mockito.when(encoder.encode(user.getPassword())).thenReturn("b");
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectNext(encoded).verifyComplete();
	}

	@Test
	void testAddUserFailReturnEmpty() {
		User user = new User();
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(userRepository.insert(user)).thenReturn(Mono.empty());
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).verifyComplete();
	}
	
	@Test
	void testAddUserReturnsEmptyIfNull() {
		Mono<User> noOne = Mono.empty();
		Mono<User> result = userService.addUser(null);
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectNext(true).verifyComplete();
	}
	
	@Test
	void testAddUserReturnsEmptyIfUserExists() {
		User user = new User();
		user.setEmail("a@a.com");
		Mono<User> noOne = Mono.empty();
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
		Mono<User> result = userService.addUser(null);
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectNext(true).verifyComplete();
	}

	@Test
	void testUpdateUser() {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		Mockito.when(userRepository.findById(1)).thenReturn(Mono.just(u1));
		Mockito.when(userRepository.save(u1)).thenReturn(Mono.just(u1));
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectNext(u1).verifyComplete();
	}

	@Test
	void testGetUserByEmailGetsUserByEmail() {
		User user = new User();
		user.setEmail("test@test.com");
		Mockito.when(userRepository.existsByEmail("test@test.com")).thenReturn(Mono.just(Boolean.TRUE));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
		Mono<User> result = userService.getUserByEmail("test@test.com");
		StepVerifier.create(result).expectNext(user).verifyComplete();
	}

	@Test
	void testGetUserByEmailFailReturnsEmpty() {
		Mockito.when(userRepository.existsByEmail("test@test.com")).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(userRepository.findByEmail("test@test.com")).thenReturn(Mono.empty());
		Mono<User> result = userService.getUserByEmail("test@test.com");
		StepVerifier.create(result).verifyComplete();
	}
}
