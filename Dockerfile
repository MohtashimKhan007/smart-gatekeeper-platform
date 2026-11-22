# Multi-stage Docker build for Smart Gatekeeper Platform
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy root gatekeeper context
COPY code/gatekeeper /app

# Build api-management-service
WORKDIR /app/api-management-service
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/api-management-service/target/*.jar app.jar

ENV PORT=8081
EXPOSE 8081

ENTRYPOINT ["java", "-XX:+TieredCompilation", "-XX:TieredStopAtLevel=1", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
