FROM openjdk:11
WORKDIR /app
COPY ./target/account-service.jar /app
EXPOSE 8085
CMD ["java", "-jar", "account-service.jar"]