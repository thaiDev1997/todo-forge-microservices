FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./target/todo-service.jar /app
EXPOSE 8084
CMD ["java", "-jar", "todo-service.jar"]