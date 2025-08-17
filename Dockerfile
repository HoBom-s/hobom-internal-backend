# 1. Project build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew --no-daemon -q clean build -x test || true

COPY src src
RUN ./gradlew --no-daemon -q build -x test

# 2. Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

COPY --from=build /app/build/libs/hobom-internal-backend.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
