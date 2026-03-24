import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    java
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.20.0"
}

group = "com.epam.learn"
version = "0.0.1-SNAPSHOT"
description = "resource-service"

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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    implementation("org.apache.kafka:kafka-clients")
    implementation(libs.apache.tika.core)
    implementation(libs.apache.tika.parsers)
    implementation(libs.springdoc)
    implementation(libs.mapstruct)
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor(libs.lombok.mapstruct.binding)
    annotationProcessor(libs.mapstruct.processor)

    runtimeOnly("org.postgresql:postgresql")
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.cloud.bom.get().toString())
    }
}

openApiGenerate {
    generatorName = "spring"

    inputSpec = layout.projectDirectory.file("src/main/resources/openapi/resource-service-api.yaml").asFile.path
    outputDir = layout.buildDirectory.file("generated/resource-api").get().asFile.path

    apiPackage = "com.epam.learn.resource.api"
    modelPackage = "com.epam.learn.resource.model"

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

tasks.register<GenerateTask>("generateSongServiceClient") {
    group = "openapi tools"
    description = "Generates a song service client"

    generatorName = "spring"
    library = "spring-http-interface"

    inputSpec = layout.projectDirectory.file("../song-service/src/main/resources/openapi/song-service-api.yaml").asFile.path
    outputDir = layout.buildDirectory.file("generated/song-client").get().asFile.path

    apiPackage = "com.epam.learn.song.client"
    modelPackage = "com.epam.learn.song.model"

    globalProperties = mapOf(
            "apis" to "",
            "models" to ""
    )

    configOptions = mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot4" to "true",
            "openApiNullable" to "false"
    )
}

val generatedSources = listOf(
        "generated/resource-api/src/main/java",
        "generated/song-client/src/main/java"
)

sourceSets {
    main {
        java {
            generatedSources.forEach { path -> srcDir(layout.buildDirectory.dir(path)) }
        }
    }
}

tasks.compileJava {
    dependsOn(tasks.openApiGenerate, tasks.named("generateSongServiceClient"))
}
