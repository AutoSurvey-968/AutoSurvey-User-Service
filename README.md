# AutoSurvey User Service

## Project Description

User Service for AutoSurvey. Controls authentication, user creation, and user management.

## Technologies Used

* Spring Webflux
* Spring Security
* Firebase Admin SDK
* AWS Keyspaces
* Eureka

## Features

* Sign-Up
* Login
* Update User
* Change Password
* Add new user
* Get user objects by email, Id

## Getting Started

**See [Primary README.md](https://github.com/AutoSurvey-968/AutoSurvey-back) for full program setup instructions.**

Requires Firebase credentials, API key, Service Account, AWS Keyspaces credentials, SQS credentials, and Eureka

Set environment variables:

* AWS_USER - Keyspaces username
* AWS_PASS - Keyspaces password
* CREDENTIALS_JSON - name of credentials json file to be placed in src/main/resources
* EUREKA_URL - default URL for eureka host
* FIREBASE_API_KEY - Firebase API key for authentication calls
* SERVICE_ACCOUNT_ID - Firebase service account id

First, clone the repository:  

SSH

```
git clone git@github.com:AutoSurvey-968/AutoSurvey-User-Service.git
```
or HTTPS
```
git clone https://github.com/AutoSurvey-968/AutoSurvey-User-Service.git
```
In main project directory, run:
```
java -jar target/{name of jar file}
```
Assuming all environment variables set, service will run.

## Usage

```
{base-url}/
```
### GET:
**Authorization level**: ADMIN-ONLY  

Gets all users in database.

### POST:
**Authorization level**: ADMIN-ONLY  

Creates a new user with default role USER. Requires firstName, lastName, email, and password fields as defined [here](src/main/java/com/revature/autosurvey/users/beans/User.java).    

Responds with 409 if email already exists in database.

### PUT
**Authorization level**: ANY  

Takes an email and password and attempts to login.

Responds with 404 if login details do not match any inside database.  

### DELETE
**Authorization** level: ANY  

Logs out user by deleting browser-side token.


```
{base-url}/email?=:email
```
### GET
**Authorization level**: ANY  

Logs out user by deleting browser-side token.
Filters database by email to find the user with the given email.  

Responds with 404 if no user found.

```
{base-url}/id
```
### GET
**Authorization level**: ADMIN-ONLY  

Get table holding next id for a certain table.


```
{base-url}/:id
```
### GET
**Authorization level**: ADMIN-ONLY  

Gets information about a specific user by their generated id.   

Responds with 404 if user with the given id is not found.

### PUT
**Authorization level**: ADMIN-ONLY  

Given a set of user information, updates the information currently in the database. Users can update their own information, but admins can update any user's information.

Responds with 404 if user with the given id is not found.

### DELETE
**Authorization level**: ADMIN-ONLY  

Deletes a user. NOTE: account with authority ROLE_SUPER_ADMIN cannot be deleted.  

Responds with 404 if user with the given id is not found.


```
{base-url}/:id/password
```
### PUT
**Authorization level**: ANY USER/ADMIN  

Updates a user's password given only an old password and new password field as detailed [here](src/main/java/com/revature/autosurvey/users/beans/PasswordChangeRequest.java). Admins can update any user's password.

Responds with 404 if user with the given id is not found.

## Contributors

- [Thomas An](https://github.com/artuis) - Primary
- [Albert Magpoc](https://github.com/albert-magpoc-revature)
- [Rashad Bowman](https://github.com/RashadCBowman)
- [Tony Touma](https://github.com/chielo9513)

