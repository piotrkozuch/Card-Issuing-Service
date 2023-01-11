FROM gradle:7.6.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:17

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/issuing-*SNAPSHOT.jar /app/issuing-service.jar

ENTRYPOINT ["java","-jar","/app/issuing-service.jar"]