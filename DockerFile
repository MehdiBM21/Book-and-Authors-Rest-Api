FROM openjdk:17-jdk-alpine
MAINTAINER mehdi
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]
