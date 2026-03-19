FROM gradle:jdk25-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle/wrapper ./gradle/wrapper
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/
COPY api-gateway/build.gradle.kts ./api-gateway/
RUN chmod +x ./gradlew && sed -i '/include(/ { /api-gateway/!d; }' settings.gradle.kts
RUN ./gradlew :api-gateway:dependencies --no-daemon || true

COPY api-gateway/src ./api-gateway/src
RUN ./gradlew :api-gateway:assemble --no-daemon -x test
RUN find ./api-gateway/build/libs -name "api-gateway-*.jar" ! -name "*-plain.jar" -exec mv {} app.jar \;

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/app.jar .
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]
