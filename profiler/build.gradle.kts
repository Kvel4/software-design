plugins {
    id("java")
    kotlin("jvm") version "1.6.21"
    id("org.springframework.boot") version "2.5.5"
//    id("io.spring.dependency-management") version "1.0.14.RELEASE"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-core:5.2.0.RELEASE")
    implementation("org.springframework:spring-context:5.2.0.RELEASE")
    implementation("org.springframework:spring-aop:5.2.0.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter:2.5.5")
    implementation("org.springframework:spring-core")
    implementation("org.aspectj:aspectjrt:1.6.11")
    implementation("org.aspectj:aspectjweaver:1.6.11")

    implementation(project(mapOf("path" to ":visitor")))
//    project(":visitor")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}