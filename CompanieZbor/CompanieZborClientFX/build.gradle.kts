plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(18)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
//    mainModule.set("org.example.companie.client")
    mainClass.set("companie.client.StartObjectClientFX")
}

javafx {
    version = "18.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":CompanieZborModel"))
    implementation(project(":CompanieZborPersistence"))
    implementation(project(":CompanieZborServices"))
    implementation(project(":CompanieZborNetworking"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.17.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.14.0")
}

tasks.test {
    useJUnitPlatform()
}