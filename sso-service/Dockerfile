
FROM gradle:jdk17-corretto-al2023 AS build
COPY --chown=gradle:gradle sso-service /home/gradle/src
COPY --chown=gradle:gradle common-lib /home/gradle/lib_src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM openjdk:17-alpine AS api_gateway
EXPOSE 8080
RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
WORKDIR /app
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]