FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY target/*.jar rk_inventory_service.jar
EXPOSE 8083:8083
ENTRYPOINT ["java", "-jar", "rk_inventory_service.jar"]