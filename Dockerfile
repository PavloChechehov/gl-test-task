FROM gradle:8.7-jdk17-alpine AS build

WORKDIR /app

COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
