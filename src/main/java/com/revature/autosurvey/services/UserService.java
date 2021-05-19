package com.revature.autosurvey.services;

import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.repos.UserRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	public void setUserRepo(UserRepo userRepo);
	
	Flux<User> getAllUsers();
	
	Mono<User> getUserByEmail(String email);
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String ID);
	
	Mono<User> deleteUser(String ID);
}
