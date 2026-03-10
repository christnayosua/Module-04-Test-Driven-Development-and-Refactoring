plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("jacoco")
    pmd
    id("org.sonarqube") version "4.4.1.3373"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "eshop"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

/* ================= CONFIGURATIONS ================= */

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

/* ================= REPOSITORIES ================= */

repositories {
    mavenCentral()
}

/* ================= SONAR ================= */

sonar {
    properties {
        property("sonar.projectKey", "christnayosua_Module-04-Test-Driven-Development-and-Refactoring")
        property("sonar.organization", "christnayosua")
        property("sonar.host.url", "https://sonarcloud.io")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml"
        )
    }
}

/* ================= PMD ================= */

pmd {
    toolVersion = "7.0.0-rc4"
    rulesMinimumPriority.set(5)
    ruleSets = listOf("category/java/bestpractices.xml")
}

/* ================= VERSIONS ================= */

val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"

/* ================= DEPENDENCIES ================= */

dependencies {

    // ===== MAIN APPLICATION =====
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // ===== DEVELOPMENT =====
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // ===== LOMBOK =====
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // ===== TESTING =====
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Selenium / Functional Test
    testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
    testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
    testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")

    // Mockito (optional, but explicit)
    testImplementation("org.mockito:mockito-junit-jupiter")
}

/* ================= TEST TASKS ================= */

tasks.register<Test>("unitTest") {
    description = "Runs unit tests."
    group = "verification"
    useJUnitPlatform()
    filter {
        excludeTestsMatching("*FunctionalTest")
    }
}

tasks.register<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"
    useJUnitPlatform()
    filter {
        includeTestsMatching("*FunctionalTest")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.test {
    filter {
        excludeTestsMatching("*FunctionalTest")
    }
    finalizedBy(tasks.jacocoTestReport)
}

/* ================= JACOCO ================= */

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}

/* ================= PMD TASK ================= */

tasks.withType<Pmd>().configureEach {
    ignoreFailures = true
}