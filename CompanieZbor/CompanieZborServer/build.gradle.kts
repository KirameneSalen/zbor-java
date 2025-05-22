plugins {
    java
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":CompanieZborModel"))
    implementation(project(":CompanieZborPersistence"))
    implementation(project(":CompanieZborServices"))
    implementation(project(":CompanieZborNetworking"))
    implementation(group = "org.apache.logging.log4j", name = "log4j-core", version = "2.17.1")
    implementation(group = "org.apache.logging.log4j", name = "log4j-api", version = "2.14.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
//    mainModule.set("org.example.companie.server")
    mainClass.set("StartObjectServer")
}