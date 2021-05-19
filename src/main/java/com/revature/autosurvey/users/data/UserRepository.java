package com.revature.autosurvey.users.data;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.users.beans.User;

public interface UserRepository extends ReactiveCassandraRepository<User, UUID> {

}
