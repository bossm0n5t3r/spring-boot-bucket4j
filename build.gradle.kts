import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.ktlint)
}

group = "me.bossm0n5t3r"
version = "0.0.1-SNAPSHOT"

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = false
    }
}

repositories {
    mavenCentral()
}

ktlint {
    version.set(
        rootProject.libs.versions.ktlint.version
            .get(),
    )
}

subprojects {
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.jvm
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.kotlin.spring
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.spring.boot
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.spring.dependency.management
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.ktlint
                .get()
                .pluginId,
    )

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.named<KotlinJvmCompile>("compileKotlin") {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
            jvmTarget = JvmTarget.JVM_21
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    ktlint {
        version.set(
            rootProject.libs.versions.ktlint.version
                .get(),
        )
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        annotationProcessor(rootProject.libs.spring.boot.configuration.processor)
        implementation(rootProject.libs.spring.context)
        implementation(rootProject.libs.spring.web)
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.jackson.module.kotlin)
        runtimeOnly(rootProject.libs.h2)

        // Bucket4j
        implementation(rootProject.libs.bucket4j.core)

        // jose4j
        implementation(rootProject.libs.jose4j)

        // kotlin-logging
        implementation(rootProject.libs.kotlin.logging)
    }
}
