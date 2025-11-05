FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
ADD jarstaging/devopsApp-2.1.2.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
