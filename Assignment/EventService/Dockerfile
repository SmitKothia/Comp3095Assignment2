# Use a base image with Java
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY build/libs/EventService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8083

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
