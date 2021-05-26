
Feature: Get Id table
Scenario: Get a table of all User Ids
    Given url 'http://localhost:8080/id'
    And def signIn = call read('classpath:com/revature/autosurvey/users/karate/loginUser.feature')
    And cookie token = signIn.authToken
    When method get
    Then status 200
    And match response == { name: '#notnull', nextId: '#notnull' }

