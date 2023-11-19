repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
