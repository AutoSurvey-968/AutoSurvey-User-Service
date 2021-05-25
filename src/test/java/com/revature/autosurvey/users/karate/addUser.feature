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
@addUsers
Feature: Add a new user - POST to /com.revature.autosurvey.users.karate

  @addUser
  Scenario: Send a request to add a new user
    Given url 'http://localhost:57323/users'
    And request { userName: 'Tony', password: 'foo' }
    When method post
    Then status 201
    And match response == { userName: '#notnull', password: '#notnull' }
