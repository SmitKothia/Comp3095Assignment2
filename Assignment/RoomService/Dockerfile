# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build directory to the container
COPY build/libs/RoomService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the service will run on
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
