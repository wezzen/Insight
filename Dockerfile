FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY ./modules/app/build/libs/app.jar app.jar

EXPOSE 8080

# Запуск приложения
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]