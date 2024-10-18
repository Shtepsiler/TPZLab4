FROM ubuntu:latest
LABEL authors="mosso"

# Use an official OpenJDK image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build file and source code into the container
COPY ./target/your-app-name.jar /app/your-app-name.jar

# Expose the port that your application will run on
EXPOSE 8080

# Set environment variables for the SQL Server connection
ENV SQL_SERVER_HOST=localhost
ENV SQL_SERVER_PORT=1433
ENV SQL_SERVER_DB=javalab4
ENV SQL_SERVER_USER=sa
ENV SQL_SERVER_PASSWORD=Qwerty123

# Run the application
ENTRYPOINT ["java", "-jar", "your-app-name.jar"]
