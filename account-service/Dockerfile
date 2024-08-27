FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./target/account-service.jar /app
EXPOSE 8085
CMD ["java", "-jar", "account-service.jar"]