# --- Stage 1: Build the Application ---
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and Maven wrapper first (for efficient caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy the source code
COPY src src

# Build the JAR file inside the container
# This skips tests to speed up deployment (remove -DskipTests if you want to run them)
RUN mvn clean package -DskipTests

# --- Stage 2: Run the Application ---
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from the 'build' stage
# Notice we copy from /app/target/ to the current directory
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]