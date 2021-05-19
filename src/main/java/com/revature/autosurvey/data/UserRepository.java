package com.revature.autosurvey.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.beans.User;

public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {

}
