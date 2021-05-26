package com.revature.autosurvey.users.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.handlers.UserHandler;
import com.revature.autosurvey.users.security.FirebaseUtil;
import com.revature.autosurvey.users.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class UserHandlerTest {
	@TestConfiguration
	static class Configuration {

		@Bean
		public UserHandler getUserHandler(UserService userService, FirebaseUtil firebaseUtil) {
			UserHandler userHandler = new UserHandler();
			userHandler.setUserService(userService);
			userHandler.setFirebaseUtil(firebaseUtil);
			return userHandler;
		}

		@Bean
		public UserService getUserService() {
			return Mockito.mock(UserService.class);
		}

		@Bean
		public FirebaseUtil getFirebaseUtil() {
			return Mockito.mock(FirebaseUtil.class);
		}

	}

	@Autowired
	FirebaseUtil firebaseUtil;

	@Autowired
	UserService userService;

	@Autowired
	UserHandler userHandler;

	@Test
	void testGetUsers() {
		User u1 = new User();
		u1.setEmail("test@hotmail.com");
		u1.setPassword("One");
		User u2 = new User();
		u2.setEmail("test@gmail.com");
		u2.setPassword("Two");
		User u3 = new User();
		u3.setEmail("test@yahoo.com");
		u3.setPassword("Three");
		Mockito.when(userService.getAllUsers()).thenReturn(Flux.just(u1, u2, u3));
		ServerRequest req = MockServerRequest.builder().build();
		Mono<ServerResponse> result = userHandler.getUsers(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
		
	}
	
	@Test
	void testGetIdTable() {
		Id i1 = new Id();
		i1.setName(Id.Name.USER);
		i1.setNextId(1);
		Id i2 = new Id();
		i2.setName(Id.Name.USER);
		i2.setNextId(2);
		Mockito.when(userService.getIdTable()).thenReturn(Flux.just(i1, i2));
		ServerRequest req = MockServerRequest.builder().build();
		Mono<ServerResponse> result = userHandler.getIdTable(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
	}
	
	
	@Test
	void testAddUser() {
		User userMock = new User();
		userMock.setEmail("taco@hotmail.com");
		userMock.setPassword("password");
		Mockito.when(userService.addUser(userMock)).thenReturn(Mono.just(userMock));
		ServerRequest req = MockServerRequest.builder().body(Mono.just(userMock));
		Mono<ServerResponse> result = userHandler.addUser(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
	}
	
	
	@Test
	void testLogin() {
		LoginRequest loginMock = new LoginRequest();
		loginMock.setEmail("text@hotmail.com");
		loginMock.setPassword("password");
		User userMock = new User();
		userMock.setId(1);
		userMock.setPassword("password");
		userMock.setEmail("text@hotmail.com");
		
		Mockito.when(userService.findByUsername(userMock.getEmail())).thenReturn(Mono.just((UserDetails) userMock));
		Mockito.when(userService.login(userMock, loginMock)).thenReturn(Mono.just(userMock));
		
		ServerRequest req = MockServerRequest.builder().body(Mono.just(loginMock));
		Mono<ServerResponse> result = userHandler.login(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
	}

	@Test
	void testGetUserById() {
		User userMock = new User();
		userMock.setId(1);
		userMock.setPassword("password");
		userMock.setEmail("text@hotmail.com");
		Mockito.when(userService.getUserById(String.valueOf(userMock.getId()))).thenReturn(Mono.just(userMock));
		ServerRequest req = MockServerRequest.builder().pathVariable("id", String.valueOf(userMock.getId())).build();
		Mono<ServerResponse> result = userHandler.getUserById(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
	}
	
	@Test
	void testGetUserByEmail() {
		User userMock = new User();
		userMock.setId(1);
		userMock.setPassword("password");
		userMock.setEmail("text@hotmail.com");
		Mockito.when(userService.getUserByEmail(userMock.getEmail())).thenReturn(Mono.just(userMock));
		ServerRequest req = MockServerRequest.builder().queryParam("email", "text@hotmail.com").build();
		Mono<ServerResponse> result = userHandler.getUserByEmail(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
		.expectComplete().verify();
	}
	
//	@Test
//	void testUpdateUser() {
//		User userMock = new User();
//		userMock.setId(1);
//		userMock.setPassword("password");
//		userMock.setEmail("text@hotmail.com");
//		Mockito.when(userService.updateUser(userMock)).thenReturn(Mono.just(userMock));
//		ServerRequest req = MockServerRequest.builder().body(Mono.just(userMock));
//		Mono<ServerResponse> result = userHandler.updateUser(req);
//		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
//		.expectComplete().verify();
//	}
	
	
//	@Test
//	void testUpdatePassword() {
//		User userMock = new User();
//		userMock.setId(1);
//		userMock.setPassword("password");
//		userMock.setEmail("text@hotmail.com");
//		Mockito.when(userService.updateUser(userMock)).thenReturn(Mono.just(userMock));
//		ServerRequest req = MockServerRequest.builder().body(Mono.just(userMock));
//		Mono<ServerResponse> result = userHandler.updateUser(req);
//		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.OK.equals(r.statusCode()))
//		.expectComplete().verify();
//	}
	
	
	@Test
	void testDeleteUser() {
		User userMock = new User();
		userMock.setId(1);
		userMock.setPassword("password");
		userMock.setEmail("text@hotmail.com");
		Mockito.when(userService.deleteUser(userMock.getId())).thenReturn(Mono.just(userMock));
		ServerRequest req = MockServerRequest.builder().pathVariable("id","1").body(Mono.just(userMock));
		Mono<ServerResponse> result = userHandler.deleteUser(req);
		StepVerifier.create(result).expectNextMatches(r -> HttpStatus.NO_CONTENT.equals(r.statusCode()))
		.expectComplete().verify();
	}
	

	@Test
	void ok() {
		User user = new User();
		Mono<ServerResponse> result = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user);
		StepVerifier.create(result).expectNextMatches(response -> HttpStatus.OK.equals(response.statusCode()))
				.expectComplete().verify();
	}


}
