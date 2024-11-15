# Step 1: Use an official JDK as a base image
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory in the container
ARG JAR_FILE=target/*.jar

# Step 3: Copy the built jar file into the container
COPY ./target/PaymentProcessingService-1.0-SNAPSHOT.jar PaymentProcessingService-1.0-SNAPSHOT.jar

# Step 4: Expose the application port
EXPOSE 8080

# Step 5: Define the command to run the jar
ENTRYPOINT ["java", "-jar", "/PaymentProcessingService-1.0-SNAPSHOT.jar"]
