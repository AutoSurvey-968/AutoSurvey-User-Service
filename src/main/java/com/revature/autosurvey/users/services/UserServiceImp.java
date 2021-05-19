package com.revature.autosurvey.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserServiceImp implements UserService{

	private UserRepository userRepository;
	
	@Autowired
	public void setUserRepo(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Flux<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Mono<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Mono<User> addUser(User user) {
		return userRepository.insert(user);
	}

	@Override
	public Mono<User> updateUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Mono<User> getUserById(String Id) {
		return userRepository.findById(Id);
	}

	@Override
	public Mono<User> deleteUser(String email) {
		userRepository.existsById("a");
		
		return userRepository.existsById(email).flatMap(bool -> {
			if(bool) {
				Mono<User> user = userRepository.findById(eMail);
				userRepository.deleteById(eMail).subscribe();
				return user;
			}
			else {
				return Mono.empty();
			}
		});
		
	}
}
