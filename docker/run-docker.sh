#!/bin/sh

# Check if autosurvey-network network exists
if [ -z "$(docker network ls -q -f name=autosurvey-network)" ]; then
    docker network create autosurvey-network
fi

# rm user-service container if it exists
if [ -n "$(docker container ls -aqf name=user-service)" ]; then
    echo "Removing user-service"
    docker container stop user-service
    docker container rm user-service
fi

#start user-service container
docker container run -d --name user-service --network autosurvey-network -e EUREKA_URL -e CREDENTIALS_JSON -e CREDENTIALS_JSON_ENCODED -e FIREBASE_API_KEY -e SERVICE_ACCOUNT_ID -e AWS_PASS -e AWS_USER -e TRUSTSTORE_PASS -e TRUSTSTORE_ENCODED -e SQS_PASS -e SQS_QUEUE_NAME -e SQS_USER autosurvey/user-service
