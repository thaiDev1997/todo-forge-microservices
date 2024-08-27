FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./target/gateway-service.jar /app
EXPOSE 8082
CMD ["java", "-jar", "gateway-service.jar"]