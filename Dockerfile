# Use official Java runtime
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and source code
COPY . .

# Build application
RUN ./mvnw clean package -DskipTests

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]
