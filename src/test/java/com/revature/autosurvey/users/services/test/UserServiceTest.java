package com.revature.autosurvey.users.services.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.revature.autosurvey.users.beans.User.Role;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.NotFoundException;
import com.revature.autosurvey.users.services.UserService;
import com.revature.autosurvey.users.services.UserServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

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
	void testDeleteUserReturnsDeletedUser() {
		User u = new User();
		u.setId(1);
		u.setEmail("a@a.com");
		
		when(userRepository.findByEmail(u.getEmail())).thenReturn(Mono.just(u));
		when(userRepository.deleteById(1)).thenReturn(Mono.empty());
		when(userRepository.existsByEmail("a@a.com")).thenReturn(Mono.just(true));
		Mono<User> result = userService.deleteUser("a@a.com");
		StepVerifier.create(result).expectNext(u).expectComplete().verify();
	}

	@Test
	void testDeleteUserReturnsEmptyIfnoUser() {
		Mono<User> noOne = Mono.empty();
		when(userRepository.existsByEmail("a")).thenReturn(Mono.just(false));
		Mono<User> result = userService.deleteUser("a");
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectError(NotFoundException.class);
	}

	@Test
	void testAddUserAddsUser() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("P4$$w0rd");
		user.setId(0);
		User encoded = new User();
		encoded.setEmail("test@test.com");
		encoded.setPassword("b");
		encoded.setId(0);
		List<Role> perms = new ArrayList<>();
		perms.add(Role.ROLE_USER);
		encoded.setAuthorities(perms);
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(idRepository.findById(Name.USER)).thenReturn(Mono.just(new Id()));
		Mockito.when(idRepository.save(Mockito.any())).thenReturn(Mono.just(new Id()));
		Mockito.when(userRepository.insert(user)).thenReturn(Mono.just(user));
		Mockito.when(encoder.encode(user.getPassword())).thenReturn("b");
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectNext(encoded).verifyComplete();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoPassword() {
		User user1 = new User();
		user1.setEmail("test@test.com");
		user1.setPassword("P$$wrdaa");
		User user2 = new User();
		user2.setEmail("test@test.com");
		user2.setPassword("P4$$w0r");
		User user3 = new User();
		user3.setEmail("test@test.com");
		user3.setPassword("P4w0rdaa");
		User user4 = new User();
		user4.setEmail("test@test.com");
		user4.setPassword("P4$$0000");
		User user5 = new User();
		user5.setEmail("test@test.com");
		user5.setPassword("p4$$w0rd");
		User user6 = new User();
		user5.setEmail("test@test.com");
		
		Mono<User> result1 = userService.addUser(user1);
		Mono<User> result2 = userService.addUser(user2);
		Mono<User> result3 = userService.addUser(user3);
		Mono<User> result4 = userService.addUser(user4);
		Mono<User> result5 = userService.addUser(user5);
		Mono<User> result6 = userService.addUser(user6);
		
		StepVerifier.create(result1).expectError();
		StepVerifier.create(result2).expectError();
		StepVerifier.create(result3).expectError();
		StepVerifier.create(result4).expectError();
		StepVerifier.create(result5).expectError();
		StepVerifier.create(result6).expectError();
	}

	@Test
	void testAddUserReturnsErrorOnNoNumberPassword() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("P$$wrdaa");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	
	@Test
	void testAddUserReturnsErrorOnShortPassword() {
		User user = new User();
		user.setEmail("test@test.com");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoSymbolPassword() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("P4w0rdaa");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoLowercasePassword() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("P4$$0000");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoUppercasePassword() {
		User user5 = new User();
		user5.setEmail("test@test.com");
		user5.setPassword("p4$$w0rd");
		User user6 = new User();
		user5.setEmail("test@test.com");
		
		Mono<User> result5 = userService.addUser(user5);
		Mono<User> result6 = userService.addUser(user6);
		
		StepVerifier.create(result5).expectError();
		StepVerifier.create(result6).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoDomainEmail() {
		User user = new User();
		user.setEmail("test@");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoAtEmail() {
		User user = new User();
		user.setEmail("testtest.com");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoUserNameEmail() {
		User user = new User();
		user.setEmail("@test.com");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoEmail() {
		User user = new User();
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError();
	}

	@Test
	void testAddUserFailReturnEmpty() {
		User user = new User();
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mockito.when(userRepository.insert(user)).thenReturn(Mono.empty());
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectError();
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
		user.setPassword("P4$$w0rd");
		Mono<User> noOne = Mono.empty();
		Mockito.when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));
//		Mockito.when(userRepository.insert(user)).thenReturn(Mono.empty());
		Mono<User> result = userService.addUser(user);
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectNext(true).verifyComplete();
	}

	@Test
	void testUpdateUser() {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		u1.setPassword("false");
		Mockito.when(userRepository.findById(1)).thenReturn(Mono.just(u1));
		Mockito.when(userRepository.save(u1)).thenReturn(Mono.just(u1));
		u1.setPassword("true");
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectNextMatches(u -> u.getPassword().equals("true")).verifyComplete();
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
