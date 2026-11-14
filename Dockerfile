# Multi-stage Dockerfile for Spring Boot Application

# 1. Build Stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the executable jar
RUN mvn clean package -DskipTests

# 2. Run Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy packaged jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render assigns dynamic PORT environment variable
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]
