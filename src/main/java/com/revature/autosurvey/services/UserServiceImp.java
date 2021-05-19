package com.revature.autosurvey.services;

import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.repos.UserRepo;


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
	public Mono<User> getUserById(String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> deleteUser(String ID) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUserRepo(UserRepo userRepo) {
		// TODO Auto-generated method stub
		
	}

}
