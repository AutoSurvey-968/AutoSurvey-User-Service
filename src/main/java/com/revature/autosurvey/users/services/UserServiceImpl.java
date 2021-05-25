package com.revature.autosurvey.users.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.shaded.guava.common.base.Objects;
import com.google.firebase.auth.FirebaseToken;
import com.revature.autosurvey.users.beans.Id;
import com.revature.autosurvey.users.beans.LoginRequest;
import com.revature.autosurvey.users.beans.PasswordChangeRequest;
import com.revature.autosurvey.users.beans.User;
import com.revature.autosurvey.users.beans.Id.Name;
import com.revature.autosurvey.users.beans.User.Role;
import com.revature.autosurvey.users.data.IdRepository;
import com.revature.autosurvey.users.data.UserRepository;
import com.revature.autosurvey.users.errors.AuthorizationError;
import com.revature.autosurvey.users.errors.NotFoundError;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private PasswordEncoder encoder;
	private IdRepository idRepository;
	
	private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
		return userRepository.existsByEmail(user.getEmail()).flatMap(bool -> {
			if (!bool.booleanValue()) {
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
		return userRepository.findById(user.getId()).flatMap(found -> {
			if (user.getPassword() != null) {
				user.setPassword(encoder.encode(user.getPassword()));
			} else {
				user.setPassword(found.getPassword());
			}
			log.debug("password: {}", user.getPassword());
			return userRepository.save(user);
		}).switchIfEmpty(Mono.error(new NotFoundError()));
	}

	@Override
	public Mono<User> getUserById(String id) {
		return userRepository.findById(Integer.parseInt(id)).switchIfEmpty(Mono.error(new NotFoundError()));
	}

	@Override
	public Mono<User> deleteUser(String email) {
		return userRepository.existsByEmail(email).flatMap(bool -> {
			if (bool.booleanValue()) {
				return userRepository.findByEmail(email).flatMap(user -> {
					if (user.getAuthorities().contains(Role.ROLE_SUPER_ADMIN)) {
						return Mono.error(new AuthorizationError());
					}
					userRepository.deleteById(user.getId()).subscribe();
					return Mono.just(user);
				});
			} else {
				return Mono.error(new NotFoundError());
			}
		});
	}

	@Override
	public Mono<UserDetails> findByUsername(String email) {
		return userRepository.existsByEmail(email).flatMap(bool -> {
			if (bool.booleanValue()) {
				return userRepository.findByEmail(email);
			} else {
				return Mono.empty();
			}
		});
	}

	@Override
	public Mono<User> login(UserDetails found, LoginRequest given) {
		return Mono.just(Boolean.valueOf(encoder.matches(given.getPassword(), found.getPassword())))
				.flatMap(correctPw -> {
					if (correctPw.booleanValue()) {
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

	@Override
	public Mono<Void> updatePassword(PasswordChangeRequest pcr, FirebaseToken fbt) {
		return userRepository.findById(pcr.getUserId()).flatMap(foundUser -> {
			if (fbt.getClaims().containsKey("roles")) {
				log.debug("password change request: {}", pcr);
				@SuppressWarnings("unchecked")
				List<Role> roles = (List<Role>) fbt.getClaims().get("roles");
				log.debug(fbt.getUid());
				if ((roles.contains(Role.ROLE_ADMIN) || fbt.getUid().equals(foundUser.getEmail()) && encoder.matches(pcr.getOldPass(), foundUser.getPassword()))) {
						foundUser.setPassword(encoder.encode(pcr.getNewPass()));
						userRepository.save(foundUser).subscribe();
						return Mono.empty();
					
				}
				log.debug("Not authorized for user: {}", foundUser);
			}
			return Mono.error(new AuthorizationError());
		});
	}

}
