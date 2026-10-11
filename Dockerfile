FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy file jar đã build sẵn từ thư mục build/libs
COPY build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]
