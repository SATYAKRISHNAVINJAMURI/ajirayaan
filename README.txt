CONTENTS OF THIS FILE
---------------------

 * Introduction
 * Requirements
 * Installation
 * Configuration
 * Server Startup
 * Troubleshooting
 * Maintainers
 
 
INTRODUCTION
------------
This Project is for  Simulating a Test environment for Ajirayaan Rover.
This is build using Spring Boot technology.

REQUIREMENTS
------------
1) Java 1.8
2) Maven for dependency management

Installation
------------
1) Unzip the ajirayaan.zip file.
2) In the root folder of ajirayaan directory execute "mvnw package"
comman so that jar will be generated.

Configuration
------------
1) There should be no services running on port 8080.
2) As this project uses that port for server installation

Server Startup
------------
1) After running "mvnw package" BUILD SUCCESFULL message
will be displayed.
2) Then you can run this below command for starting the server
by opening the command prompt in target folder which will be in the
ajirayaan folder:
"java -jar ajirayaan-0.0.1-SNAPSHOT.jar"
3) If the server didn't start, follow the troble shooting guide.

Troubleshooting
------------
1) If server startup failed. Check port no 8080 and stop the
services running over there.
2) If you don't want to do step 1 you can add a line
in the file application.properties at location
ajirayaan\src\main\resources\

Add the line server.port=8081 at the end of this file.

Maintainers
------------
Name : Satya Krishna Vinjamuri
contact: 9490537556

