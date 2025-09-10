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

The application are built with in mind be able to scale in a good way. But at some point the scaling must
be done from outside the application. Then it would be good to run it in Kubernetes and just add more pods.


## API
An API documentation for the endpoints can be found at http://localhost:8080/swagger-ui/index.html when the service are started

## Start service local

When below are done (the dependencies section) you are able to start the service locally

### Start crypto-recommendation-service
You can either start the service from your IDE. For IntelliJ Idea you create a configuration of 
type Application, point at the main class (`Application.class`) and start by press the play button.
Or you can start the image...

### Start dependencies
The `crypto-recommendation-service` are dependent of external tools, a Postgres database. To setup a Postgres database do:
* Run the script `start-services.sh` under ./scripts. It will download and start a Docker container with Postgres.
* Run the script `create-database.sh` under ./scripts. It will create necessary database.

### Possible improvements
* Add functionality to be able to inject `supportedCryptos` and `blockedIps` via environment variables. That 
would be good if the service exists in a Kubernetes cluster etc. Then it is possible to do configuration without 
rebuild the image
* Add for example a Kafka consumer or a POST endpoint to be able to update data. CVS files are not manageable
when the system grows