package com.revature.autosurvey.users.beans;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.stereotype.Component;

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

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public int getNextId() {
		return nextId;
	}

	public void setNextId(int nextId) {
		this.nextId = nextId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nextId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Id other = (Id) obj;
		if (name != other.name)
			return false;
		if (nextId != other.nextId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Id [name=" + name + ", nextId=" + nextId + "]";
	}
}
