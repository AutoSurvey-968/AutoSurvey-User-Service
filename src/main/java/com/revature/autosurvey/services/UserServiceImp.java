package com.revature.autosurvey.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserServiceImp implements UserService{
	
	private UserRepository userRepo;
	
	@Autowired
	public void setUserRepo(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public Flux<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> addUser(User user) {
		return userRepo.insert(user);
	}

	@Override
	public Mono<User> updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> getUserById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> deleteUser(String Id) {
		// TODO Auto-generated method stub
		return null;
	}	

}
