FROM openjdk:21-jdk-slim

# Install wget
RUN apt-get update && apt-get install -y wget

# Set the working directory in the container
WORKDIR /CodeRacing/CodeRacingP2PTest


# Copy the project files into the container
COPY . .

# Download the Gson JAR file from Maven Central
RUN wget https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.8/gson-2.8.8.jar -P /CodeRacing/lib/

# Compile Java files with Gson included in the classpath
RUN javac -cp ".:/CodeRacing/lib/gson-2.8.8.jar:/CodeRacing/lib/*" CodeRacingP2PTest/src/*.java

# Run the application
CMD ["java", "-cp", ".:/CodeRacing/CodeRacingP2PTest/lib/gson-2.8.8.jar:/CodeRacing//CodeRacingP2PTestlib/*", "Host"]
