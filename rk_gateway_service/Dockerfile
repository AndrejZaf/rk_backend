FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_gateway_service.jar
EXPOSE 8080:8080
ENTRYPOINT ["java", "-jar", "rk_gateway_service.jar"]