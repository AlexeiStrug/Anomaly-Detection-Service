# Use the official Maven image as the build environment
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/demo-*-SNAPSHOT.jar myapp.jar
EXPOSE ${SERVER_PORT}
CMD ["java", "-jar", "myapp.jar"]
