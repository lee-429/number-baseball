# 1. 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS build
COPY . .
RUN ./gradlew clean build -x test

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]