# crypto-recommendation-service
Piktiv backend exercise: Crypto Investment

## Start service local

When below are done (the dependencies section) you are able to start the service locally

### crypto-recommendation-service
You can either start the service from your IDE. For IntelliJ Idea you create a configuration of 
type Application, point at the main class (`Application.class`) and start by press the play button.
Or you can start the image...

### Dependencies
The `crypto-recommendation-service` are dependent of external tools, a Postgres database. To setup a Postgres database do:
* Run the script `start-services.sh` under ./scripts. It will download and start a Docker container with Postgres.
* Run the script `create-database.sh` under ./scripts. It will create necessary database.

