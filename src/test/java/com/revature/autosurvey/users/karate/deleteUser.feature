Feature: Delete user
Scenario: Send a request to delete a user

    Given url 'http://localhost:8080/'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And request { id: '6' }
    And cookie token = signIn.authToken
    When method delete
    Then status 204


