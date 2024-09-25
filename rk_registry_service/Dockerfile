FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_registry_service.jar
EXPOSE 8761:8761
ENTRYPOINT ["java", "-jar", "rk_registry_service.jar"]