package com.revature.autosurvey.users.beans;


import java.util.Collection;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Component
@Table
public class User implements UserDetails {

	private static final long serialVersionUID = 1120343599093192914L;
	
	public enum Role implements GrantedAuthority {
		ROLE_USER, ROLE_ADMIN;

		@Override
		public String getAuthority() {
			return name();
		}
	}
	
	@PrimaryKey
	@Column
	private String email;
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column
	private String username;
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column
	private String password;
	@Column
	private List<Role> authorities;
	@Column
	private boolean enabled;
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column
	private boolean credentialsNonExpired;
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column
	private boolean accountNonLocked;
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column
	private boolean accountNonExpired;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getUsername() {
		return username;
	}
	public void setUsername(String name) {
		this.username = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (accountNonExpired ? 1231 : 1237);
		result = prime * result + (accountNonLocked ? 1231 : 1237);
		result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
		result = prime * result + (credentialsNonExpired ? 1231 : 1237);
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (accountNonExpired != other.accountNonExpired)
			return false;
		if (accountNonLocked != other.accountNonLocked)
			return false;
		if (authorities == null) {
			if (other.authorities != null)
				return false;
		} else if (!authorities.equals(other.authorities))
			return false;
		if (credentialsNonExpired != other.credentialsNonExpired)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", username=" + username + ", authorities=" + authorities + ", enabled="
				+ enabled + ", credentialsNonExpired=" + credentialsNonExpired + ", accountNonLocked="
				+ accountNonLocked + ", accountNonExpired=" + accountNonExpired + "]";
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	
}
