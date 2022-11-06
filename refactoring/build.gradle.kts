plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jetty:jetty-servlet:11.0.12")
    implementation("org.eclipse.jetty:jetty-server:11.0.12")
    implementation("org.postgresql:postgresql:42.3.7")
    implementation("com.zaxxer:HikariCP:5.0.1")
//    implementation("javax.servlet:javax.servlet-api:3.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.testcontainers:postgresql:1.17.3")
    testImplementation("org.testcontainers:junit-jupiter:1.17.5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}