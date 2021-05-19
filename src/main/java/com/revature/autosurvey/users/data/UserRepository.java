package com.revature.autosurvey.users.data;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.users.beans.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCassandraRepository<User, String> {
	
	@AllowFiltering
	Mono<User> findbyUserName(String userName);
}
