plugins {
    java
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.20.0"
}

group = "com.epam.learn"
version = "0.0.1-SNAPSHOT"
description = "song-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(libs.springdoc)
    implementation(libs.mapstruct)
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor(libs.lombok.mapstruct.binding)
    annotationProcessor(libs.mapstruct.processor)

    runtimeOnly("org.postgresql:postgresql")
}

openApiGenerate {
    generatorName = "spring"

    inputSpec = layout.projectDirectory.file("src/main/resources/openapi/song-service-api.yaml").asFile.path
    outputDir = layout.buildDirectory.file("generated/song-api").get().asFile.path

    apiPackage = "com.epam.learn.song.api"
    modelPackage = "com.epam.learn.song.model"

    globalProperties = mapOf(
            "apis" to "",
            "models" to ""
    )

    configOptions = mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot4" to "true",
            "useTags" to "true",
            "openApiNullable" to "false",
            "skipDefaultInterface" to "true"
    )
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.file("generated/song-api/src/main/java"))
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate)
}
