package com.revature.autosurvey.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Objects;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.Id.Name;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.User.Role;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.IllegalEmailException;
import com.revature.autosurvey.users.errors.IllegalPasswordException;
import com.revature.autosurvey.users.errors.NotFoundException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

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
		return userRepository.findByEmail(email);
	}

	@Override
	public Mono<User> addUser(User user) {
		if (Objects.equal(user, null)) {
			return Mono.empty();
		}
		
		if (user.getEmail() == null) {
			return Mono.error(new IllegalEmailException("Empty Email Field"));
		}
		
		Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
		
		Matcher emailMatcher = emailPattern.matcher(user.getEmail());
		if (!emailMatcher.matches()) {
			return Mono.error(new IllegalPasswordException("Entry is not an Email"));
		}
		
		
		if (user.getPassword() == null) {
			return Mono.error(new IllegalPasswordException("Empty password Field"));
		}
		
		Pattern passwordPattern = Pattern.compile(".*(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,}");

		Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
		if (!passwordMatcher.matches()) {
			return Mono.error(new IllegalPasswordException("Invalid Password"));
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
					return idRepository.save(id).flatMap(nextId -> userRepository.insert(user));
				});
			} else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<User> updateUser(User user) {
		
		if (user.getPassword() == null) {
			return Mono.error(new IllegalPasswordException("Empty password Field"));
		}
		
		Pattern passwordPattern = Pattern.compile(".*(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,}");

		Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
		if (!passwordMatcher.matches()) {
			return Mono.error(new IllegalPasswordException("Invalid Password"));
		}
		
		return userRepository.findById(user.getId())
				.flatMap(found -> userRepository.save(found))
				.switchIfEmpty(Mono.error(new NotFoundException()));
	}

	@Override
	public Mono<User> getUserById(String id) {
		return userRepository.findById(Integer.parseInt(id))
				.switchIfEmpty(Mono.error(new NotFoundException()));
	}

	@Override
	public Mono<User> deleteUser(String email) {
		return userRepository.existsByEmail(email).flatMap(bool -> {
			if (bool) {
				return userRepository.findByEmail(email).map(user -> {
					userRepository.deleteById(user.getId()).subscribe();
					return user;
				});
			} else {
				return Mono.error(new NotFoundException());
			}
		});
	}

	@Override
	public Mono<UserDetails> findByUsername(String email) {
		return userRepository.existsByEmail(email).flatMap(bool -> {
			if (bool) {
				return userRepository.findByEmail(email);
			} else {
				return Mono.error(new NotFoundException());
			}
		});
	}

	@Override
	public Mono<User> login(UserDetails found, LoginRequest given) {
		return Mono.just(Boolean.valueOf(encoder.matches(given.getPassword(), found.getPassword())))
				.flatMap(correctPw -> {
					if (correctPw) {
						return Mono.just(found).cast(User.class);
					} else {
						return Mono.error(new NotFoundException());
					}
				});
	}

	@Override
	public Flux<Id> getIdTable() {
		return idRepository.findAll();
	}

}
