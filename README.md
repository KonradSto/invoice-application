# Invoices Application

Simple accounting system. Application for invoices management.

##Installation

* Use JDK, version 1.8 or higher
* Open application with your IDE, we recommend You to use maven project in IntelliJ IDEA
* Generate binding classes from src\main\resources\invoices.xsd with jax2b plugin
* Run ```mvn clean verify``` to assure that application works properly

## Database

Supported databases:
  * InMemory
  * InFile
  * Hibernate
  
  To choose database use 'application.properties' file:
 
  ```
  database = memory
  ```
  ```
  database = in-file
  ```
  ```
  database = hibernate
  ```
  
## Logger

To enable [logging level](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Level.html) setup appropriate log level in 'application.properties' file:
```
logging.level.pl.coderstrust=DEBUG
```

## Swagger UI

Run the application and open your browser at a specified URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Technology Stack

* [Java SE 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/) - Dependency Management
* [Hibernate](http://hibernate.org) - mapping an object-oriented domain model to a relational database
* [Spring boot](https://spring.io/projects/spring-boot)
* [Spring Security](https://spring.io/projects/spring-security) - authentication and access-control framework
* [Spring Data](https://spring.io/projects/spring-data)
* [JUnit](https://junit.org)
* [Mockito](https://site.mockito.org/)
* [Jacoco](https://www.eclemma.org/jacoco) - maintaining code coverage over 8.0 and branch coverage over 0.5
* [Checkstyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/)
* [Logger](https://logging.apache.org/log4j/2.x/)
* [Swagger](https://swagger.io/)

## Authors

Project implemented as part of the [CodersTrust](https://coderstrust.pl/) Java - First Project course - *9th edition*.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
