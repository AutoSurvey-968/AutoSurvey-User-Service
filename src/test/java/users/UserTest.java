package users;

import com.intuit.karate.junit5.Karate;

public class UserTest {

	@Karate.Test
	Karate testLoginUser() {
		return Karate.run("loginUser").relativeTo(getClass());
	}
}
