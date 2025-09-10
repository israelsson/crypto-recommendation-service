# crypto-recommendation-service
Piktiv backend exercise: Crypto Investment

## System design
The application are build with Hexagonal design pattern, and are because of that easy to switch between different
technics for incoming and outgoing data.

First time the application start up it will migrate all data from the cvs files into a Postgres database.
A database is faster and will scale better when the application grows.

It is designed to only support the currencies that are specified in `application.yaml` and the `supportedCryptos` 
list. If any other currency are requested the application will send an error message to the user. And if a new 
currency should be supported, just add it in the list.

It is possible to block malicious users based on their IP. Just add their IP to the `blockedIps` 
list in `application.yaml`.

There is a specific properties file (`application-docker.yaml`). It is necessary to be able to set some values 
only when it is run from inside docker.

The application are built with in mind be able to scale in a good way. But at some point the scaling must
be done from outside the application. Then it would be good to run it in Kubernetes and just add more pods.


## API
An API documentation for the endpoints can be found at http://localhost:8080/swagger-ui/index.html when the service are started

## Start service local

There are two ways to start the service:
* Start everything in Docker
* Start the service locally in your IDE and the database in Docker

### Start everything in Docker
* Navigate to the project root in a terminal
* Run `docker-compose up --build`. It will create an image of the service and start it when it is ready. It
will also start a Postgres database container.
* The start of the service will probably fail because there is no database yet.
* Navigate to the script folder `<project-root>/scripts` in your terminal
* Run the `create-database.sh` script in your terminal. It will create the database in the Docker container
* (Make sure the service are started correctly)
* Now you can use your browser and navigate to http://localhost:8080/swagger-ui/index.html to see the endpoint documentation
* You can also test the endpoints from that page

### Start database in Docker and service from IDE
#### Start dependencies
The `crypto-recommendation-service` are dependent of external tools, a Postgres database. To set up a 
Postgres database do:
* Navigate to the project in your terminal
* Run the script `start-services.sh` under ./scripts. It will download and start a Docker container with Postgres.
* Run the script `create-database.sh` under ./scripts. It will create necessary database.

#### Start crypto-recommendation-service
For IntelliJ Idea you create a configuration of type Application (or Spring boot), point at the 
main class (`Application.class`) and start by press the play button.

### Possible improvements
* Add functionality to be able to inject `supportedCryptos` and `blockedIps` via environment variables. That 
would be good if the service exists in a Kubernetes cluster etc. Then it is possible to do configuration without 
rebuild the image
* Add for example a Kafka consumer or a POST endpoint to be able to update data. CVS files are not manageable
when the system grows