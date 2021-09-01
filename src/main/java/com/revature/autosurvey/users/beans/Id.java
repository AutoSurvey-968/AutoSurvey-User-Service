package com.revature.autosurvey.users.beans;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@Table
public class Id {

	@PrimaryKey
	@Column
	private Name name;
	@Column
	private int nextId;
	
	public enum Name {
		USER;
	}

	public Id() {
		super();
	}
	
}
