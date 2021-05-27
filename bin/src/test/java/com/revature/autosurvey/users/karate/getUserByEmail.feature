Feature: Get a user by their email
Scenario: Get User object by query parameter email
    Given url 'http://localhost:8080'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And cookie token = signIn.authToken
    And request { email: 'test2' }
    When method get
    Then status 200
    And match response == {"firstName":first,"lastName":last,"credentialsNonExpired":true,"accountNonExpired":true,"id":2,"authorities":['ROLE_USER'],"enabled":true,"accountNonLocked":true,"email":test2}