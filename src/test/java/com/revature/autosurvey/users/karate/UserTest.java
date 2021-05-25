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
		//tests.add("classpath:/com/revature/autosurvey/users/karate/addUser.feature");
		//tests.add("classpath:/com.revature.autosurvey.users.karate/deleteUser.feature");
		//tests.add("classpath:/com.revature.autosurvey.users.karate/getUserById.feature");
		//tests.add("classpath:/com.revature.autosurvey.users.karate/getUserEmail.feature");
		tests.add("classpath:/com/revature/autosurvey/users/karate/loginUser.feature");
		//tests.add("classpath:/com.revature.autosurvey.users.karate/updateUser.feature");
		//tests.add("classpath:/com/revature/autosurvey/surveys/karate/survey-post.feature");
		//tests.add("classpath:/com/revature/autosurvey/surveys/karate/survey-put.feature");
		tests.add("classpath:/com/revature/autosurvey/users/karate/deleteUser.feature");
		Results results = Runner.path(tests).parallel(5);
		assertEquals(0, results.getFailCount(), results.getErrorMessages());
	}
}