import java.util.Properties

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.21.0"
    id("nu.studer.jooq") version "9.0"
    id("org.flywaydb.flyway") version "9.22.3"
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

fun loadEnvProps(): Properties {
    val props = Properties()
    val envFile = rootProject.file(".env")
    if (envFile.exists()) {
        envFile.inputStream().use { props.load(it) }
    }
    return props
}

val jooqVersion = "3.20.5"

jooq {
    version.set(jooqVersion)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/bear"
                    user = ""
                    password = ""
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "bear"
                    }
                    target.apply {
                        packageName = "com.example.jooq.generated"
                        directory = "$buildDir/generated/jooq"
                    }
                    generate.apply {
                        isDaos = false
                        isPojos = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                }
            }
        }
    }
}

flyway {
    val props = loadEnvProps()

    url = "jdbc:postgresql://localhost:5432/bear"
    user = props.getProperty("DB_USER") ?: System.getenv("DB_USER") ?: "postgres"
    password = props.getProperty("DB_PASSWORD") ?: System.getenv("DB_PASSWORD") ?: "postgres"
    locations = arrayOf("filesystem:src/main/resources/db/migration")
    schemas = arrayOf("bear")
    baselineOnMigrate = true
    baselineVersion = "3"
}

tasks.named("generateJooq") {
    doFirst {
        val props = loadEnvProps()
        val jooqConfig = (project.extensions.getByName("jooq") as nu.studer.gradle.jooq.JooqExtension)
            .configurations.getByName("main").jooqConfiguration

        jooqConfig.jdbc.user = props.getProperty("DB_USER") ?: System.getenv("DB_USER") ?: error("DB_USER not set")
        jooqConfig.jdbc.password = props.getProperty("DB_PASSWORD") ?: System.getenv("DB_PASSWORD") ?: error("DB_PASSWORD not set")
    }
}

group = "com.hobom"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("io.github.openfeign:feign-jackson:13.2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // jOOQ Codegen runtime
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")
    jooqGenerator("org.jooq:jooq-meta:$jooqVersion")
    jooqGenerator("org.jooq:jooq:$jooqVersion")
    jooqGenerator("org.postgresql:postgresql:42.7.3")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:1.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
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

sourceSets["main"].java.srcDir("$buildDir/generated/jooq")
