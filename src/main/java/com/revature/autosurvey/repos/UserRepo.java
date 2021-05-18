package com.revature.autosurvey.repos;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.beans.User;

public interface UserRepo extends ReactiveCassandraRepository<User, UUID>{

}
