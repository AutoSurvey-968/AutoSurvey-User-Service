package com.revature.autosurvey.users.services;

import java.util.UUID;

import com.revature.autosurvey.users.beans.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	Flux<User> getAllUsers();
	
	Mono<User> getUserByEmail(String email);
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String Id);

	Mono<User> deleteUser(User user);


}
