FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_order_service.jar
EXPOSE 8084:8084
ENTRYPOINT ["java", "-jar", "rk_order_service.jar"]