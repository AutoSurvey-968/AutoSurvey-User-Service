# AutoSurvey User Service

## Project Description

Here goes your awesome project description!

## Technologies Used

* Spring Webflux
* Spring Security
* Firebase Admin SDK
* AWS SQS
* AWS Keyspaces
* Eureka

## Features

* Sign-Up
* Login
* Update User
* Change Password

To-do list:
* Delete User

## Getting Started
   
Requires Firebase credentials, API key, Service Account, AWS Keyspaces credentials, SQS credentials, and Eureka

Set environment variables for:

AWS_PASS - Keyspaces username
AWS_USER - Keyspaces password
CREDENTIALS_JSON - name of credentials json file to be placed in src/main/resources
EUREKA_URL - default URL for eureka host
FIREBASE_API_KEY - Firebase API key for authentication calls
SERVICE_ACCOUNT_ID - Firebase service account id
SQS_USER - SQS username
SQS_PASS - SQS password

First, clone the repository:

SSH

```
git clone git@github.com:AutoSurvey-968/AutoSurvey-User-Service.git
```
or HTTPS
```
git clone https://github.com/AutoSurvey-968/AutoSurvey-User-Service.git
```
in main project directory, run:
```
java -jar target/{name of jar file}
```
Assuming all environment variables set, service will run

## Usage



## Contributors

> Here list the people who have contributed to this project. (ignore this section, if its a solo project)

## License

This project uses the following license: [<license_name>](<link>).
