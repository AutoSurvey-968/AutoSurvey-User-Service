package com.revature.autosurvey.users.services;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.firebase.auth.FirebaseToken;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.PasswordChangeRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService {
	
	public void setUserRepo(UserRepository userRepo);
	
	Flux<User> getAllUsers();
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String id);

	Mono<User> deleteUser(Integer id);

	Mono<User> getUserByEmail(String email);

	public Mono<User> login(UserDetails found, LoginRequest given);

	public Flux<Id> getIdTable();

	public Mono<Void> updatePassword(PasswordChangeRequest pcr, FirebaseToken fbt);

}
