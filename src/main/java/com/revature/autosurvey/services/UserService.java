package com.revature.autosurvey.services;

import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	public void setUserRepo(UserRepository userRepo);
	
	Flux<User> getAllUsers();
	
	Mono<User> getUserByEmail(String email);
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String Id);
	
	Mono<User> deleteUser(String Id);
}
