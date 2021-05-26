Feature: Get User By Id
Scenario: Get a user by path variable Id
    Given url 'http://localhost:8080'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And cookie token = signIn.authToken
    And request { id: '2' }
    When method get
    Then status 200
    And match response == {"firstName":first,"lastName":last,"credentialsNonExpired":true,"accountNonExpired":true,"id":2,"authorities":['ROLE_USER'],"enabled":true,"accountNonLocked":true,"email":test2}

