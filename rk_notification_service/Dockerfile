FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_notification_service.jar
EXPOSE 8086:8086
ENTRYPOINT ["java", "-jar", "rk_notification_service.jar"]