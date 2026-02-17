FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle gradle
RUN chmod +x gradlew
COPY . .
RUN ./gradlew --no-daemon --stacktrace --warning-mode all clean bootJar -x test \
 && ls -l build/libs \
 && cp build/libs/*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S -G app app
USER app:app

COPY --from=build /workspace/app.jar ./app.jar
EXPOSE 8081
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar app.jar"]
