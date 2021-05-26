Feature: Update a user - PUT to /users/id
Scenario: Update the user whose Id is in the path variable

    Given url 'http://localhost:8080/6'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And cookie token = signIn.authToken
    And request { firstName: 'Tony', lastName: 'Touma', credentialsNonExpired: 'true', accountNonExpired: 'true', username: 'change@change.test', authorities: ['ROLE_USER'], accountNonExpired: 'true', accountNonLocked: 'true', enabled: 'true' }
    When method put
    Then status 200
    And match response == {"firstName":Tony,"lastName":Touma,"credentialsNonExpired":true,"accountNonExpired":true,"id":6,"authorities":["ROLE_USER"],"enabled":true, "accountNonLocked": true, "email":"change@change.test"}

