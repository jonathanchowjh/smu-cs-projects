# spring-jpa-boilerplate

### RUNNING the CODE
* Start MAMP server
* run the sql code at ```/sql/tables.sql```
* run ```./mvnw spring-boot:run```

### Testing the code
#### Option 1: curl from terminal
```
// ADD USER
curl --location --request POST 'localhost:8080/add?name=name&email=email'
// FIND ALL USERS
curl --location --request GET 'localhost:8080/all'
```
#### Option 2: use postman (recommended)
* Download Desktop app from https://www.postman.com/downloads/
* Open App
* ADD USER
1. Change request type to POST
2. insert url ```localhost:8080/add?name=name&email=email```
* FIND ALL USERS
1. Change request type to GET
2. insert url ```localhost:8080/all```# auth-microservice
# Ryver_Banking_Platform
