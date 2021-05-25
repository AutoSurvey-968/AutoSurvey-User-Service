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
Feature: Delete user

  @tag1
  Scenario: Send a request to delete a user
    Given url 'http://localhost:8080/com.revature.autosurvey.users.karate/{id}'
    ##we will need to check that the user doing the deletion has rights to do so##
    When method delete
    Then status 200
    ##I'm not sure if this next part is necessary or not.  TBD##
    And match response == { id: '#notnull' }

