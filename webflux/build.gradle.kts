repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":core"))

    implementation(rootProject.libs.spring.boot.starter.data.r2dbc)
    implementation(rootProject.libs.spring.boot.starter.webflux)

    implementation(rootProject.libs.reactor.kotlin.extensions)

    implementation(rootProject.libs.kotlinx.coroutines.reactor)

    runtimeOnly(rootProject.libs.r2dbc.h2)

    testImplementation(rootProject.libs.spring.boot.starter.test)
    testImplementation(rootProject.libs.reactor.test)
}
