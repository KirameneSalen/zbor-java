plugins {
    id("java")
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":CompanieZborModel"))
    implementation(project(":CompanieZborPersistence"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.17.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.14.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation(group="com.fasterxml.jackson.core", name="jackson-annotations", version="2.15.0")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0")
    runtimeOnly(group="org.xerial", name="sqlite-jdbc", version="3.34.0")
}

tasks.test {
    useJUnitPlatform()
}