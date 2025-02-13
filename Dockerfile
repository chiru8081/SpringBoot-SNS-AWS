FROM openjdk:17-jdk-slim
# Set working directory inside the container
WORKDIR /app
# Copy the compiled Java application JAR file into the container
COPY ./target/springboot-sns.jar /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/springboot-sns.jar"]