FROM gradle:jdk25-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle/wrapper ./gradle/wrapper
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/
COPY song-service/build.gradle.kts ./song-service/
RUN chmod +x ./gradlew && sed -i '/include(/ { /song-service/!d; }' settings.gradle.kts
RUN ./gradlew :song-service:dependencies --no-daemon || true

COPY song-service/src ./song-service/src
RUN ./gradlew :song-service:assemble --no-daemon -x test
RUN find ./song-service/build/libs -name "song-service-*.jar" ! -name "*-plain.jar" -exec mv {} app.jar \;

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/app.jar .
CMD ["java", "-jar", "app.jar"]
