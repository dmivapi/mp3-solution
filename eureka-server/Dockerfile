FROM gradle:jdk25-alpine AS build

WORKDIR /app

COPY gradlew .
COPY gradle/wrapper ./gradle/wrapper
COPY settings.gradle.kts .
COPY gradle/libs.versions.toml ./gradle/
COPY eureka-server/build.gradle.kts ./eureka-server/
RUN chmod +x ./gradlew && sed -i '/include(/ { /eureka-server/!d; }' settings.gradle.kts
RUN ./gradlew :eureka-server:dependencies --no-daemon || true

COPY eureka-server/src ./eureka-server/src
RUN ./gradlew :eureka-server:assemble --no-daemon -x test
RUN find ./eureka-server/build/libs -name "eureka-server-*.jar" ! -name "*-plain.jar" -exec mv {} app.jar \;

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/app.jar .
EXPOSE 8761
CMD ["java", "-jar", "app.jar"]
