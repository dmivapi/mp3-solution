FROM gradle:jdk25-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle/wrapper ./gradle/wrapper
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/
COPY resource-service/build.gradle.kts ./resource-service/
COPY song-service/src/main/resources/openapi/song-service-api.yaml ./song-service/src/main/resources/openapi/
RUN chmod +x ./gradlew && ./gradlew :resource-service:dependencies --no-daemon || true

COPY resource-service/src ./resource-service/src
RUN ./gradlew :resource-service:assemble --no-daemon -x test
RUN find ./resource-service/build/libs -name "resource-service-*.jar" ! -name "*-plain.jar" -exec mv {} app.jar \;

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/app.jar .
EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
