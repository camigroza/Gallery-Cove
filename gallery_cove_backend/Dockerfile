# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-21-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application, skipping tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Set database connection properties as environment variables
ENV DATABASE_URL=jdbc:mysql://monorail.proxy.rlwy.net:56032/railway
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=rSVIjbTyfhdvsNHKzxelFKxxVrJkAbIv


# Run the application
CMD ["java", "-jar", "app.jar"]
