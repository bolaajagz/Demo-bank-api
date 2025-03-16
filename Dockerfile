# Use official Java runtime
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and source code
COPY . .

# Grant execute permission to Maven Wrapper
RUN chmod +x mvnw

# Build application
RUN ./mvnw clean package -DskipTests

# Expose application port
EXPOSE 8080

# Run the application
RUN mv target/bankingDemo-0.0.1-SNAPSHOT.jar.original target/bankingDemo.jar
CMD ["java", "-jar", "target/bankingDemo.jar"]

