#!/bin/sh
#Decode the passed in truststore env variable
mkdir -p src/main/resources
echo "$TRUSTSTORE_ENCODED" | base64 -d > src/main/resources/cassandra_truststore.jks
java -jar user-service.jar