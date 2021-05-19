package com.revature.autosurvey.users.users.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.autosurvey.users.beans.User;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	private User emptyUser;
	
	@PostMapping
	public void addUser(User user) {
//		return userService.addUser(user).defaultIfEmpty(emptyUser).map(user -> {
//			if(user.getUsername() == null) {
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
//			}
//			return ResponseEntity.status(HttpStatus.CREATED).body(user);
//		});
	}
	
	@PutMapping
	public void login() {
		
	}
	
	@GetMapping
	public void getUserById() {
		
	}
	
	@GetMapping("")
	public void getUserEmail() {
		
	}
	
	@PutMapping("/{id}")
	public void updateUser() {
		
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser() {
		
	}

}
