package com.revature.autosurvey.users.services.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.users.data.UserRepository;


import com.revature.autosurvey.users.services.UserService;
import com.revature.autosurvey.users.services.UserServiceImp;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	
	@TestConfiguration
	static class Config{
		
		@Bean

		public UserService getUserService(UserRepository userRepository){
			UserServiceImp usi = new UserServiceImp();
			usi.setUserRepo(userRepository);
			return usi;
		}
		
		@Bean
		public UserRepository getUserRepo() {
			return Mockito.mock(UserRepository.class);
		}
	}
}
