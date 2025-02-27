repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":core"))

    implementation(rootProject.libs.spring.boot.starter.data.jpa)
    implementation(rootProject.libs.spring.boot.starter.web)

    testImplementation(rootProject.libs.spring.boot.starter.test)
}
