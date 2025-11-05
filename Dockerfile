FROM openjdk:17-jdk-slim
WORKDIR /app
ADD jarstaging/devopsApp-2.1.2.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
