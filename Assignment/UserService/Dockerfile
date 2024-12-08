# Start with an official OpenJDK image as the base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build artifact (JAR file) from the host to the container
COPY build/libs/UserService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the service runs
EXPOSE 8081

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
