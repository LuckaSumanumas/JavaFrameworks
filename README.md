# Java Frameworks Rest API

The API retrieves a list of 10 most active (as defined by the number of stars) Java frameworks.  
It also allow starring/un-starring a repo.

### How to run the application
Build jar file by executing this command:  
`mvn install`  
Run the application by executing this command:  
`java -jar target/javaframeworks-0.0.1-SNAPSHOT.jar com.deepmedia.javaframeworks.JavaFrameworksApplication` 

It should be also possible to run the application by creating docker image:  
`docker run image-tag`  
or even  
`docker-compose up`

### Test the application 
Application could be tested by invoiking these endpoints:  
GET http://localhost:8085/api/java-frameworks?metric=starCount  
metric could be starcount or numofcontr  

PATCH http://localhost:8085/api/java-frameworks/{name}  
with payload example:
{
    "owner": "spring-projects",
    "star": true
}  

It should by passed these headers in the request for authenticating against GitHub: username, password

Open API documentation could be access by this link   
http://localhost:8085/swagger-ui.html  


