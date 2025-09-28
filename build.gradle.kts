import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar
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
    if (envFile.exists()) envFile.inputStream().use { props.load(it) }
    return props
}

group = "com.hobom"
version = "0.0.1-SNAPSHOT"

java {
    toolchain { languageVersion = JavaLanguageVersion.of(21) }
}

repositories { mavenCentral() }

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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:3.20.5")
    implementation("io.github.openfeign:feign-jackson:13.2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // jOOQ Codegen runtime
    jooqGenerator("org.jooq:jooq-codegen:3.20.5")
    jooqGenerator("org.jooq:jooq-meta:3.20.5")
    jooqGenerator("org.jooq:jooq:3.20.5")
    jooqGenerator("org.jooq:jooq-meta-extensions:3.20.5")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testImplementation("com.tngtech.archunit:archunit-junit5-engine:1.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

dependencyManagement {
    imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}") }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> { useJUnitPlatform() }

tasks.named<BootJar>("bootJar") {
    archiveFileName.set("hobom-internal-backend.jar")
}

val jooqGenDir = "$buildDir/generated/jooq/main"

jooq {
    version.set("3.20.5")
    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator = org.jooq.meta.jaxb.Generator()
                    .withName("org.jooq.codegen.KotlinGenerator")
                    .withDatabase(
                        org.jooq.meta.jaxb.Database()
                            .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                            .withProperties(
                                listOf(
                                    org.jooq.meta.jaxb.Property().withKey("scripts")
                                        .withValue("src/main/resources/db/migration/*.sql"),
                                    org.jooq.meta.jaxb.Property().withKey("sort").withValue("true"),
                                    org.jooq.meta.jaxb.Property().withKey("defaultNameCase").withValue("lower"),
                                    org.jooq.meta.jaxb.Property().withKey("sql-dialect").withValue("Postgres"),
                                ),
                            ),
                    )
                    .withTarget(
                        org.jooq.meta.jaxb.Target()
                            .withPackageName("org.jooq.generated")
                            .withDirectory(jooqGenDir),
                    )
            }
        }
    }
}

kotlin.sourceSets.getByName("main").kotlin.srcDir(jooqGenDir)

tasks.withType<KotlinCompile>().configureEach {
    dependsOn("generateJooq")
}

flyway {
    val props = loadEnvProps()
    url = props.getProperty("HOBOM_DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/bear"
    user = props.getProperty("DB_USER") ?: System.getenv("DB_USER") ?: "postgres"
    password = props.getProperty("DB_PASSWORD") ?: System.getenv("DB_PASSWORD") ?: "postgres"
    locations = arrayOf("filesystem:src/main/resources/db/migration")
    schemas = arrayOf("public")
    baselineOnMigrate = true
    baselineVersion = "3"
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
