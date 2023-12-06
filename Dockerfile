# Use an official OpenJDK runtime as a parent image
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory
WORKDIR /app

# Copy Maven wrapper and POM file
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Copy the project files
COPY . .

# Build the project
RUN ./mvnw clean install

# Copy the application JAR file
COPY target/my-spring-boot-app.jar /app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app.jar"]
