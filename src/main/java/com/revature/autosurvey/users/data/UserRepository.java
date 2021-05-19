package com.revature.autosurvey.users.data;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.users.beans.User;

import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveCassandraRepository<User, String> {
	
	@AllowFiltering
<<<<<<< HEAD
	Mono<User> findbyUsername(String username);
=======
	Mono<User> findByUsername(String username);
>>>>>>> e9162d5fd9732210cb0715fd9a58e5f1a6969a48

	@AllowFiltering
	Mono<Boolean> existsByUsername(String username);
}
