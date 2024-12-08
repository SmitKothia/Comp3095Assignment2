# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY build/libs/BookingService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8084

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
