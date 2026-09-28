FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew \
    && ./gradlew --no-daemon bootJar \
    && find build/libs -maxdepth 1 -type f -name '*.jar' ! -name '*-plain.jar' -exec cp {} /app.jar \; \
    && test -f /app.jar

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=build /app.jar ./app.jar

RUN useradd --system --create-home --uid 10001 appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
