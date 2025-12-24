FROM eclipse-temurin:17

# Set working directory
WORKDIR /app

# Copy the JAR file (make sure it exists in target/)
COPY target/*.jar app.jar

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]