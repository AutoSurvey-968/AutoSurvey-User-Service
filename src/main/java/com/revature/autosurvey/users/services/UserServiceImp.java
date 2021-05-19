package com.revature.autosurvey.users.services;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserServiceImp implements UserService{

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
		// TODO Auto-generated method stub
		return null;
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

	public void setUserRepo(UserRepository userRepo) {
		// TODO Auto-generated method stub
		
	}

}
