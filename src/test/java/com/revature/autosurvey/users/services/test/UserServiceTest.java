package com.revature.autosurvey.users.services.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.Id.Name;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.User.Role;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.AuthorizationError;
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

		when(userRepository.findAll()).thenReturn(uFlux);
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
		when(userRepository.existsById(1)).thenReturn(Mono.just(false));
		Mono<User> result = userService.deleteUser(1);
		StepVerifier.create(result).expectError(NotFoundError.class).verify();
	}
	
	@Test
	void testDeleteUserThrowsErrorIfNotAuthorized() {
		User u = new User();
		u.setEmail("a@a.com");
		List<Role> rList = new ArrayList<>();
		rList.add(Role.ROLE_SUPER_ADMIN);
		u.setAuthorities(rList);
		when(userRepository.existsById(1)).thenReturn(Mono.just(true));
		when(userRepository.findById(1)).thenReturn(Mono.just(u));
		Mono<User> result = userService.deleteUser(1);
		StepVerifier.create(result).expectError(AuthorizationError.class).verify();
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
		encoded.setAccountNonExpired(true);
		encoded.setEnabled(true);
		encoded.setAccountNonLocked(true);
		encoded.setCredentialsNonExpired(true);
		List<Role> perms = new ArrayList<>();
		perms.add(Role.ROLE_USER);
		encoded.setAuthorities(perms);
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		when(idRepository.findById(Name.USER)).thenReturn(Mono.just(new Id()));
		when(idRepository.save(Mockito.any())).thenReturn(Mono.just(new Id()));
		when(userRepository.insert(user)).thenReturn(Mono.just(user));
		when(encoder.encode(user.getPassword())).thenReturn("b");
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectNext(encoded).verifyComplete();
	}
	
	@Test
	void testAddUserReturnsErrorOnNoPassword() {
		User user = new User();
		user.setEmail("test@test.com");
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError().verify();
	}

	@ParameterizedTest
	@ValueSource(strings = {"4Nother1", "@Notherone", "@NOTHER1", "@nother1", "@Nother1"})
	void testAddUserReturnsErrorOnBadPassword(String password) {
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword(password);
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError().verify();
	}
	
	@ParameterizedTest
	@ValueSource(strings = { "test@", "testtest.com", "@test.com"})
	void testAddUserReturnsErrorOnBadEmail(String email) {
		User user = new User();
		user.setEmail(email);
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError().verify();
	}

	@Test
	void testAddUserReturnsErrorOnNoEmail() {
		User user = new User();
		
		Mono<User> result = userService.addUser(user);
		
		StepVerifier.create(result).expectError().verify();
	}

	@Test
	void testAddUserFailReturnError() {
		User user = new User();
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		when(userRepository.insert(user)).thenReturn(Mono.empty());
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	void testAddUserReturnsEmptyIfNull() {
		Mono<User> noOne = Mono.empty();
		Mono<User> result = userService.addUser(null);
		Mono<Boolean> comparer = Mono.sequenceEqual(result, noOne);
		StepVerifier.create(comparer).expectNext(true).verifyComplete();
	}
	
	@Test
	void testAddUserReturnsErrorIfUserExists() {
		User user = new User();
		user.setEmail("a@a.com");
		Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(Mono.just(true));
		Mono<User> result = userService.addUser(user);
		StepVerifier.create(result).expectError().verify();
	}

	@Test
	void testUpdateUser() {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		u1.setPassword("P4$$w0rd");
		List<Role> rList = new ArrayList<User.Role>();
		rList.add(Role.ROLE_SUPER_ADMIN);
		u1.setAuthorities(rList);
		Mockito.when(userRepository.findById(1)).thenReturn(Mono.just(u1));
		Mockito.when(userRepository.save(u1)).thenReturn(Mono.just(u1));
		Mockito.when(userRepository.existsByEmail(u1.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectNext(u1).verifyComplete();
	}
	
	@Test
	void testUpdateUserWithNoPassword() {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		User u2 = new User();
		u2.setId(1);
		u2.setEmail("text@text.com");
		u2.setPassword("P4$$w0rd");
		List<Role> rList = new ArrayList<User.Role>();
		rList.add(Role.ROLE_SUPER_ADMIN);
		u1.setAuthorities(rList);
		Mockito.when(userRepository.findById(1)).thenReturn(Mono.just(u2));
		Mockito.when(userRepository.save(u1)).thenReturn(Mono.just(u2));
		Mockito.when(userRepository.existsByEmail(u1.getEmail())).thenReturn(Mono.just(Boolean.FALSE));
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectNext(u2).verifyComplete();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"4Nother1", "@Notherone", "@NOTHER1", "@nother1", "@N0ther"})
	void testUpdateUserReturnsErrorOnBadPassword(String password) {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		u1.setPassword(password);
		when(userRepository.findById(1)).thenReturn(Mono.just(u1));
		when(userRepository.save(u1)).thenReturn(Mono.just(u1));
		u1.setPassword(password);
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	void testUpdateUserReturnsErrorOnNonExistingUser() {
		User u1 = new User();
		u1.setId(1);
		u1.setEmail("text@text.com");
		u1.setPassword("P4$$w0rd");
		when(userRepository.findById(1)).thenReturn(Mono.empty());
//		Mockito.when(userRepository.save(u1)).thenReturn(Mono.just(u1));
		u1.setPassword("@Nother1");	
		Mono<User> result = userService.updateUser(u1);
		StepVerifier.create(result).expectError().verify();
	}

	@Test
	void testGetUserByEmailGetsUserByEmail() {
		User user = new User();
		user.setEmail("test@test.com");
		when(userRepository.existsByEmail("test@test.com")).thenReturn(Mono.just(Boolean.TRUE));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Mono.just(user));
		Mono<User> result = userService.getUserByEmail("test@test.com");
		StepVerifier.create(result).expectNext(user).verifyComplete();
	}

	@Test
	void testGetUserByEmailFailReturnsEmpty() {
		when(userRepository.existsByEmail("test@test.com")).thenReturn(Mono.just(Boolean.FALSE));
		when(userRepository.findByEmail("test@test.com")).thenReturn(Mono.empty());
		Mono<User> result = userService.getUserByEmail("test@test.com");
		StepVerifier.create(result).verifyComplete();
	}
	
	@Test
	void testGetUserIdGetsAUser() {
		User user = new User();
		user.setId(0);
		String id = "0";
		when(userRepository.findById(Integer.parseInt(id))).thenReturn(Mono.just(user));
		
		Mono<User> result = userService.getUserById("0");
		
		StepVerifier.create(result).expectNext(user).verifyComplete();
	}
	
	@Test
	void testGetUserIdReturnsErrorWhenEmpty() {
		String id = "0";
		when(userRepository.findById(Integer.parseInt(id))).thenReturn(Mono.empty());
		
		Mono<User> result = userService.getUserById("0");
		
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	void testFindByUserNameFindsUser() {
		String email = "a";
		User u = new User();
		u.setEmail(email);
		
		when(userRepository.existsByEmail(email)).thenReturn(Mono.just(Boolean.TRUE));
		when(userRepository.findByEmail(email)).thenReturn(Mono.just(u));
		
		Mono<UserDetails> result = userService.findByUsername(email);
		
		StepVerifier.create(result).expectNext(u).verifyComplete();
	}
	
	@Test
	void testFindByUserNameReturnsErrorWhenNoUser() {
		String email = "a";
		User u = new User();
		u.setEmail(email);
		
		when(userRepository.existsByEmail(email)).thenReturn(Mono.just(Boolean.FALSE));
		
		Mono<UserDetails> result = userService.findByUsername(email);
		
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	void testloginReturnsUser() {
		User user = new User();
		user.setEmail("a");
		user.setPassword("b");
		UserDetails found = user;
		LoginRequest given = new LoginRequest();
		given.setEmail("a");
		given.setPassword("b");
		
		when(encoder.matches(given.getPassword(), found.getPassword())).thenReturn(true);
		
		Mono<User> result = userService.login(found, given);
		
		StepVerifier.create(result).expectNext(user).verifyComplete();
	}
	
	@Test
	void testLoginReturnsErrorOnNoUser() {
		User user = new User();
		user.setEmail("a");
		user.setPassword("b");
		UserDetails found = user;
		LoginRequest given = new LoginRequest();
		given.setEmail("b");
		given.setPassword("a");
		
		when(encoder.matches(given.getPassword(), found.getPassword())).thenReturn(false);
		
		Mono<User> result = userService.login(found, given);
		
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	void testGetIdTableFindsAll() {
		Id id1 = new Id();
		id1.setName(Name.USER);
		id1.setNextId(2);
		Id id2 = new Id();
		id2.setName(Name.USER);
		id2.setNextId(3);
		Id id3 = new Id();
		id3.setName(Name.USER);
		id3.setNextId(4);
		Id id4 = new Id();
		id4.setName(Name.USER);
		id4.setNextId(5);
		
		when(idRepository.findAll()).thenReturn(Flux.just(id1,id2,id3,id4));
		
		Flux<Id> result = userService.getIdTable();
		
		StepVerifier.create(result).expectNext(id1,id2,id3,id4);
	}
	
//	Dont Know how to test this cuz firebaseToken constructor is default and the class is final
//	@ParameterizedTest
//	@ValueSource(strings = {"4Nother1", "@Notherone", "@NOTHER1", "@nother1", "@N0ther"})
//	void testUpdatePasswordUpdatesPasswword(String password) {
//		PasswordChangeRequest pcr = new PasswordChangeRequest();
//		pcr.setNewPass(password);
//		Map<String, Object> fbtMap = new HashMap<>();
//		List<Role> rList = new ArrayList<>();
//		rList.add(Role.ROLE_ADMIN);
//		fbtMap.put("roles", rList);
//		FirebaseToken fbt;
//		
//	}
}
