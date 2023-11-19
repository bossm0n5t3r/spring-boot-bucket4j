import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.20"
    val springFrameworkBootVersion = "3.1.5"
    val springDependencyManagementVersion = "1.1.3"
    val ktlintGradleVersion = "11.6.1"

    id("org.springframework.boot") version springFrameworkBootVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintGradleVersion
}

private val ktlintVersion: String by project
private val bucket4jVersion: String by project
private val jose4jVersion: String by project
private val kotlinLoggingVersion: String by project

group = "me.bossm0n5t3r"
version = "0.0.1-SNAPSHOT"

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = false
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    ktlint {
        version.set(ktlintVersion)
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        implementation("org.springframework:spring-context")
        implementation("org.springframework:spring-web")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        runtimeOnly("com.h2database:h2")

        // Bucket4j
        implementation("com.bucket4j:bucket4j-core:$bucket4jVersion")

        // jose.4.j
        implementation("org.bitbucket.b_c:jose4j:$jose4jVersion")

        // kotlin-logging
        implementation("io.github.oshai:kotlin-logging-jvm:$kotlinLoggingVersion")
    }
}
