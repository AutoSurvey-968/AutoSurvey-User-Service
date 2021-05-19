package com.revature.autosurvey.users.services;

<<<<<<< HEAD:src/main/java/com/revature/autosurvey/services/UserService.java
import com.revature.autosurvey.beans.User;
import com.revature.autosurvey.data.UserRepository;
=======
import java.util.UUID;

import com.revature.autosurvey.users.beans.User;
>>>>>>> 7b927fa2491ef04103c611b560cd6930170ca0ab:src/main/java/com/revature/autosurvey/users/services/UserService.java

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	public void setUserRepo(UserRepository userRepo);
	
	Flux<User> getAllUsers();
	
	Mono<User> getUserByEmail(String email);
	
	Mono<User> addUser(User user);
	
	Mono<User> updateUser(User user);
	
	Mono<User> getUserById(String Id);

	Mono<Void> deleteUser(User user);


}
