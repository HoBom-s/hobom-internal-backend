plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.21.0"

    kotlin("plugin.jpa") version "1.9.25"
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.50.0")
        trimTrailingWhitespace()
        endWithNewline()
        indentWithSpaces()
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }
}

group = "com.hobom"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    runtimeOnly("com.mysql:mysql-connector-j")

    // test implementation
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:1.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<Test>("archUnitTest") {
    description = "Run all ArchUnit tests under com.hobom.hobominternal"
    group = "verification"

    useJUnitPlatform()

    include("**/*ArchitectureTest.class")
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}
tasks.register("formatKotlin") {
    group = "formatting"
    description = "Formats all Kotlin source files using Spotless"

    dependsOn("spotlessApply")
}

tasks.register("lintKotlin") {
    group = "formatting"
    description = "Checks Kotlin code style using Spotless"

    dependsOn("spotlessCheck")
}
