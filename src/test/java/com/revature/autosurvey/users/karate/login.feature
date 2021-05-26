#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Login as User - PUT to /com.revature.autosurvey.users.karate

  @tag1
  Scenario: Send a request and login successfully
    Given url 'http://localhost:8080/'
    And request {"password":"hunter2","email":"admin@admin.admin"}
    When method put
    Then status 200
    And match response == {"firstName":null,"lastName":null,"credentialsNonExpired":false,"accountNonExpired":false,"id":4,"email":"admin@admin.admin","authorities":["ROLE_USER","ROLE_ADMIN","ROLE_SUPER_ADMIN"],"enabled":false,"accountNonLocked":false,"username":"admin@admin.admin"}
		And match responseCookies contains { token: '#notnull' }
		And def authToken = responseCookies.token