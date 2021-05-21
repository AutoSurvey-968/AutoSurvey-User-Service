FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY target/user-service.jar .
COPY src/main/resources/cassandra_truststore.jks src/main/resources/cassandra_truststore.jks

ENV AWS_USER=Richard-Orr-Batch-968-at-855430746673
ENV AWS_PASS=g0DEaZhrkX4StYoysa+IISZ7vQgRbnKm0EGuG16L2b0=
ENV CREDENTIALS_JSON=richard-orr-firebase-service-account.json.json
ENV FIREBASE_API_KEY=AIzaSyC4sxZlT-McTildwtxa8LV1lj7ZQhzOrs0
ENV SERVICE_ACCOUNT_ID=project3-firebase-auth-viewer@training-team-253916.iam.gserviceaccount.com

EXPOSE 8085

CMD ["java", "-jar", "user-service.jar"]