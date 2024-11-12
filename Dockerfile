# Start from a Java base image
FROM mcr.microsoft.com/playwright/java:v1.48.0

# Set the working directory
WORKDIR /app

# Copy the project files to the Docker image
COPY . .

# Install any dependencies (if you use Maven or Gradle)
RUN mvn install -DskipTests

# Define the command to run the Playwright tests
CMD ["mvn", "test"]
