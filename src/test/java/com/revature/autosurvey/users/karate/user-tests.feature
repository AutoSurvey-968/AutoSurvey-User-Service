@user-tests
Feature: tests all the functions of the UserService

Background:
* url 'http://localhost:8080'

Scenario:
Login as an admin user
Add a new user
Get the user by email
Get the user by Id
Update the user
Update the users password
Delete the user

##PUT - login (in this case, make sure the user being logged in is admin for following tests to work)
Given path '/'
And request {"password":"hunter2","email":"admin@admin.admin"}
When method put
Then status 200
And match response contains {"firstName":null,"lastName":null,"credentialsNonExpired":false,"accountNonExpired":false,"id":4,"email":"admin@admin.admin","authorities":["ROLE_USER","ROLE_ADMIN","ROLE_SUPER_ADMIN"],"enabled":false,"accountNonLocked":false}
And match responseCookies contains { token: '#notnull' }
* def authToken = responseCookies.token

##POST - add a new user
Given path '/'
And request { email: 'foo7', password: 'bar' }
And cookie token = authToken
When method post
Then status 200
And match response contains { email: '#notnull'}

* def userEmail = response.email
* def id = response.id

##GET - get the freshly made user by query param: email
Given path '/'
And param email = userEmail
And cookie token = authToken
When method get
Then status 200
And match response contains {email: '#notnull'}

##GET - get a different user by path param: Id
Given path '/' + id
And cookie token = authToken
When method get
Then status 200
And match response == '#notnull'

##PUT - update the user using path param: id
Given path '/' + id
And cookie token = authToken
And request { firstName: 'Tony', lastName: 'Touma', credentialsNonExpired: 'true', accountNonExpired: 'true', email: 'change@change.test', authorities: ['ROLE_USER'], accountNonExpired: 'true', accountNonLocked: 'true', enabled: 'true' }
When method put
Then status 200
And match response contains {"firstName":Tony,"lastName":Touma,"credentialsNonExpired":true,"accountNonExpired":true,"authorities":["ROLE_USER"],"enabled":true, "accountNonLocked": true, "email":"change@change.test"}

##PUT - update the user password. path: {id}/password
Given path '/' + id + '/password' 
And cookie token = authToken
And request { password: 'bar2' }
When method put
Then status 204


##DELETE - delete the user from the db by Id
Given path '/' + id
And cookie token = authToken
When method delete
Then status 204

