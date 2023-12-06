# Stage 1: Build the application
FROM adoptopenjdk:11-jdk-hotspot as build
WORKDIR /app
COPY . .
RUN chmod +x mvnw   # Grant execute permission to the script
RUN ./mvnw clean install

# Stage 2: Run the application
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app
COPY --from=build /app/target/my-spring-boot-app.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]
