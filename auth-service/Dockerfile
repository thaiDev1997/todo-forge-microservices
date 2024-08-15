FROM openjdk:11
WORKDIR /app
COPY ./target/auth-service.jar /app
EXPOSE 8083
CMD ["java", "-jar", "auth-service.jar"]