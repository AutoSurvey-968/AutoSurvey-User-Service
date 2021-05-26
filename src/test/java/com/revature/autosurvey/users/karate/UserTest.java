//package com.revature.autosurvey.users.karate;
//
//import com.intuit.karate.junit5.Karate;
//
//public class UserTest {
//
////	@Karate.Test
////	Karate testLoginUser() {
////		return Karate.run("loginUser").relativeTo(getClass());
////	}
//	
//	
//}
  
package com.revature.autosurvey.users.karate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;

class UserTest {

	List<String> tests = new ArrayList<>();

	@Test
	void testParallel() {
		System.setProperty("karate.env", "dev");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/getIdTable.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/getUserById.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/updateUser.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/deleteUser.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/updatePassword.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/getUserByEmail.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/getUsers.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/addUser.feature");
		//tests.add("classpath:/com/revature/autosurvey/users/karate/login.feature");
		tests.add("classpath:/com/revature/autosurvey/users/karate/user-tests.feature");
		Results results = Runner.path(tests).parallel(5);
		assertEquals(0, results.getFailCount(), results.getErrorMessages());
	}
}