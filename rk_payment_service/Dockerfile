FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_payment_service.jar
EXPOSE 8085:8085
ENTRYPOINT ["java", "-jar", "rk_payment_service.jar"]