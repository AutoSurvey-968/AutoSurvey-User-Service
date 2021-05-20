package com.revature.autosurvey.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
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
		return userRepository.findById(email);
	}

	@Override
	public Mono<User> addUser(User user) {
		return Mono.just(user);
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
		return userRepository.existsById(email).flatMap(bool -> {
			if(bool) {
				Mono<User> user = userRepository.findById(email);
				userRepository.deleteById(email).subscribe();
				return user;
			}
			else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<User> login(UserDetails found, User given) {
		return Mono.just(found.getPassword().equals(given.getPassword())).flatMap(correctPw -> {
			if (correctPw) {
				return Mono.just(found).cast(User.class);
			} else {
				return Mono.error(new UserNotFoundError());
			}
		});
	}

}
