FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN chmod +x gradlew

COPY . .
RUN ./gradlew --no-daemon clean bootJar -x test \
 && ls build/libs \
 && cp build/libs/*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app && adduser -S app -G app
USER app:app
WORKDIR /app

COPY --from=build /workspace/app.jar ./app.jar

ENV SERVER_PORT=8081 \
    SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS=""

EXPOSE 8081

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar app.jar"]
