package com.revature.autosurvey.users.services;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	public void setUserRepo(UserRepository userRepo);
	
	Flux<User> getAllUsers();
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String Id);

	Mono<User> deleteUser(String userName);

}
