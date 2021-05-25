Feature: makes a call to login feature in order to login as admin and then: Add a new user - POST to /com.revature.autosurvey.users.karate
Scenario: Send a request to add a new user

    Given url 'http://localhost:8080/'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And request { email: 'tony@tony.tony', password: 'foo' }
    And cookie token = signIn.authToken
    When method post
    Then status 200
    And match response == { userName: '#notnull', password: '#notnull' }
