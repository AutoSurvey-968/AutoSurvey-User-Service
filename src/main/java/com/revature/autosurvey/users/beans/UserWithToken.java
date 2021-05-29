package com.revature.autosurvey.users.beans;

import java.util.List;

import org.springframework.beans.BeanUtils;

public class UserWithToken extends User {

	private static final long serialVersionUID = -87467209632178467L;
	
	private String token;
	
	public UserWithToken() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	public UserWithToken(User u, String token) {
		BeanUtils.copyProperties(u, this);
		this.setAuthorities((List<Role>) u.getAuthorities());
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserWithToken other = (UserWithToken) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
	
}
