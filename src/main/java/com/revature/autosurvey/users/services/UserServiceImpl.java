package com.revature.autosurvey.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Objects;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.data.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService{

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	
	@Autowired
	public void setUserRepo(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Autowired
	public void setPasswordEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
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
		if(Objects.equal(user, null)) {
			return Mono.empty();
		}
		
		return userRepository.existsById(user.getEmail()).flatMap(bool ->{
			if(bool) {
				user.setPassword(encoder.encode(user.getPassword()));
				return userRepository.insert(user);
			} else {
				return Mono.empty();
			}
		});
				
				
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

}
