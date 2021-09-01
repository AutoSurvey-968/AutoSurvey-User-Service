package com.revature.autosurvey.users.beans;


import java.util.Collection;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Component
@Table
public class User implements UserDetails {

	private static final long serialVersionUID = 1120343599093192914L;
	
	public enum Role implements GrantedAuthority {
		ROLE_SUPER_ADMIN, ROLE_USER, ROLE_ADMIN;

		@Override
		public String getAuthority() {
			return name();
		}
	}
	
	@PrimaryKey
	@Column
	private int id;
	
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String email;
	@Column
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	@Column
	private List<Role> authorities;
	@Column
	private boolean enabled;
	@Column
	private boolean credentialsNonExpired;
	@Column
	private boolean accountNonLocked;
	@Column
	private boolean accountNonExpired;
	
	public User() {
		super();
		
	}
	
	// leaving since i think it has to do with the security framework

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

	@JsonIgnore
	@Override
	public String getUsername() {
		return this.email;
	}
	
}
