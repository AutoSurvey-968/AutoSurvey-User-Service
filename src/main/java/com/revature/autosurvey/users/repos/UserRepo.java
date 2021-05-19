package com.revature.autosurvey.users.repos;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.users.beans.User;

public interface UserRepo extends ReactiveCassandraRepository<User, UUID>{

}
