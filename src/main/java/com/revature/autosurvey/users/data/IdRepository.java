package com.revature.autosurvey.users.data;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import com.revature.autosurvey.users.beans.Id;

public interface IdRepository extends ReactiveCassandraRepository<Id, Id.Name>{

}
