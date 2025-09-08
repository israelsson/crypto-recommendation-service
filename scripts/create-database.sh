#!/usr/bin/env bash

source common.sh

DOCKER_CONTAINER=postgres
DB=piktiv-crypto-service
PG_USER=postgres
HOST=localhost

DB_RESOURCES_DIR=../src/main/resources/db

echo "Removing current db"
docker exec -i ${DOCKER_CONTAINER} dropdb ${DB} --username=${PG_USER}

echo "Creating new db"
docker exec -i ${DOCKER_CONTAINER} createdb ${DB} --username=${PG_USER}
