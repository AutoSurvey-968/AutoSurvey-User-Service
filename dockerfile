FROM openjdk:8-jdk-alpine
WORKDIR /app

COPY target/user-service.jar .
COPY src/main/resources/cassandra_truststore.jks src/main/resources/
EXPOSE 8080

CMD [ "java", "-jar", "user-service.jar" ]