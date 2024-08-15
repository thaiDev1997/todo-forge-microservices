FROM openjdk:11
WORKDIR /app
COPY ./target/config-service.jar /app
EXPOSE 8080
CMD ["java", "-jar", "config-service.jar"]