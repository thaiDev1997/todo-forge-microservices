FROM openjdk:11
WORKDIR /app
COPY ./target/gateway-service.jar /app
EXPOSE 8082
CMD ["java", "-jar", "gateway-service.jar"]