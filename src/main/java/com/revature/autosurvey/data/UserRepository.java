package com.revature.autosurvey.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.beans.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {

	Mono<User> findByEmail(String email);

}
