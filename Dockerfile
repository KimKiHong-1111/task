FROM gradle:8.2.1-jdk17 AS build
WORKDIR /app

# 필수 파일 복사
COPY build.gradle settings.gradle gradlew ./
COPY gradle/wrapper gradle/wrapper
COPY src src

# 권한 설정 및 빌드
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
