import io.github.cdimascio.dotenv.dotenv

buildscript {
    dependencies {
        classpath("io.github.cdimascio:dotenv-kotlin:6.4.1")
    }
}

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"

    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.21.0"
    id("nu.studer.jooq") version "9.0"

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

val dotenv = dotenv {
    directory = rootDir.toString()
    ignoreIfMissing = false
}

val dbUser = dotenv["DB_USER"] ?: error("Missing DB_USER")
val dbPassword = dotenv["DB_PASSWORD"] ?: error("Missing DB_PASSWORD")
val jooqVersion = "3.20.5"
jooq {
    version.set(jooqVersion)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306/bear?autoReconnect=true&useUnicode=true&serverTimezone=Asia/Seoul"
                    user = dbUser
                    password = dbPassword
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    runtimeOnly("com.mysql:mysql-connector-j")

    // jOOQ
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:$jooqVersion")
    jooqGenerator("org.jooq:jooq-codegen:$jooqVersion")
    jooqGenerator("org.jooq:jooq-meta:$jooqVersion")
    jooqGenerator("org.jooq:jooq:$jooqVersion")
    jooqGenerator("com.mysql:mysql-connector-j:8.2.0")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // test implementation
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        // for mockk
        exclude(module = "mockito-core")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:1.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // mockk
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
