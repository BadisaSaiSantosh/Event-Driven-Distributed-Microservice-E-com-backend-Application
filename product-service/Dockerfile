# === product-service/Dockerfile ===

# Stage 1: Build the application using Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the JAR file, skipping tests to make the build faster
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar
# Expose the port defined in application.yml
EXPOSE 8082
# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]