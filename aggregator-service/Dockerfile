# Use Java 21 Alpine as the base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/aggregator-service-0.0.1-SNAPSHOT.jar application.jar

# Expose the port your application uses
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "application.jar"]