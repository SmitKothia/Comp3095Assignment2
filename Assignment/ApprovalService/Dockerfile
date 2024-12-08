# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file
COPY build/libs/ApprovalService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8085

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
