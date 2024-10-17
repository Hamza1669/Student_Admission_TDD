# Step 1: Use a base image with JDK installed
FROM openjdk:17-jdk

# Step 2: Add a volume to be able to store log files or any other data
VOLUME /tmp

# Step 3: Expose the port that the application will run on
EXPOSE 8080

# Step 4: Copy the JAR file into the container
COPY target/Student_Admission_TDD-0.0.1-SNAPSHOT.jar /app.jar

# Step 5: Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
