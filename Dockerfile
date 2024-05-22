FROM openjdk:17-jdk-slim

WORKDIR /app
COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY src ./src
RUN ./gradlew build --no-daemon

COPY build/libs/*.jar app.jar
EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "app.jar"]