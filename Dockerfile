FROM amazoncorretto:21-alpine3.19-jdk
LABEL authors="hoyeon"

ARG JAR_FILE

WORKDIR /app

COPY ${JAR_FILE} /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
