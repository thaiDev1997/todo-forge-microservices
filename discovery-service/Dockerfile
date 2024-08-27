FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./target/discovery-service.jar /app
EXPOSE 8081
CMD ["java", "-jar", "discovery-service.jar"]