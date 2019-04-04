FROM openjdk:8u201-jdk-alpine3.9

COPY . /wizardofjokes

RUN cd /wizardofjokes && ./gradlew build

FROM openjdk:8u201-jre-alpine3.9

COPY --from=0 /wizardofjokes/build/libs/WizardOfJokes-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
