FROM openjdk:8u201-jre-alpine3.9

ARG JAR_FILE=build/libs/WizardOfJokes-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
