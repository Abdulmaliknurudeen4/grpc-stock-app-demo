# Use Java 21 Alpine as the base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY stock-service.jar application.jar

# Expose the port your application uses
EXPOSE 6565

# Command to run the application
CMD ["java", "-jar", "application.jar"]