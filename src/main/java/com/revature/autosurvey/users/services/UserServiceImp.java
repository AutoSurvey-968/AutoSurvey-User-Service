package com.revature.autosurvey.users.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Objects;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.Id.Name;
import com.revature.autosurvey.users.beans.User.Role;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.NotFoundError;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImp implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private IdRepository idRepository;

	@Autowired
	public void setUserRepo(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	@Autowired
	public void setIdRepository(IdRepository idRepository) {
		this.idRepository = idRepository;
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
		if (Objects.equal(user, null)) {
			return Mono.empty();
		}
		return userRepository.existsByEmail(user.getEmail()).flatMap(bool -> {
			if (!bool) {
				return idRepository.findById(Name.USER).flatMap(id -> {
					user.setPassword(encoder.encode(user.getPassword()));
					user.setId(id.getNextId());
					List<Role> perms = new ArrayList<>();
					perms.add(Role.ROLE_USER);
					user.setAuthorities(perms);
					id.setNextId(id.getNextId() + 1);
					idRepository.save(id).subscribe();
					return userRepository.insert(user);
				});
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
			if (bool) {
				Mono<User> user = userRepository.findById(email);
				userRepository.deleteById(email).subscribe();
				return user;
			} else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<UserDetails> findByUsername(String email) {
		return userRepository.existsByEmail(email).flatMap(bool -> {
			if (bool) {
				return userRepository.findByEmail(email);
			} else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<User> login(UserDetails found, LoginRequest given) {
		return Mono.just(Boolean.valueOf(encoder.matches(given.getPassword(), found.getPassword()))).flatMap(correctPw -> {
			if (correctPw) {
				return Mono.just(found).cast(User.class);
			} else {
				return Mono.error(new NotFoundError());
			}
		});
	}

	@Override
	public Flux<Id> getIdTable() {
		return idRepository.findAll();
	}

}
