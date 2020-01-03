#Test app
###1. Technologies
* Java 1.8
* Spring Boot 2.2.2.RELEASE
* Gradle 5.2.1
* Redis

###2. To Run this project
Preparation before launching:
1. install and launch _[Redis]_ database
2. install _[Gradle]_ 

[Gradle]: https://gradle.org/install
[Redis]: https://redis.io/topics/quickstart

To build and run a project:
1. command to build app: _gradle build_
2. command to run app: _java -jar build/libs/testGradle-1.0-SNAPSHOT.jar_

At now, the project should be launched.

###3. To Use this project
This project was created as a CRUD application.<br/>
There are four methods available:
1. GET ```http://localhost:8080/customerAccount``` to see customerAccount list<br/>
2. GET ```http://localhost:8080/customerAccount/id``` to see customerAccount by id<br/>
3. POST ```http://localhost:8080/customerAccount``` to create a new customerAccount<br/>
4. PUT ```http://localhost:8080/customerAccount``` to update customerAccount<br/>
5. DELETE ```http://localhost:8080/customerAccount/id``` to delete customerAccount by id<br/>