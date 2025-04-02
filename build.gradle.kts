plugins {
    id("java")
    id("net.ltgt.errorprone") version "4.1.0"
    id("io.freefair.lombok") version "8.13.1"
    id("org.springframework.boot") version "3.4.4" apply false
    id ("io.spring.dependency-management") version "1.1.7"
    jacoco
}

group = "com.github.wezzen.insight"
version = "0.0.1"

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "io.freefair.lombok")



    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")

        implementation("com.h2database:h2:2.3.232")
        implementation("org.postgresql:postgresql:42.7.5")

        implementation("io.github.cdimascio:dotenv-java:3.2.0")

        testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.4")
        testImplementation("nl.jqno.equalsverifier:equalsverifier:3.19.2")
        testImplementation(platform("org.junit:junit-bom:5.12.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("org.mockito:mockito-core:5.16.1")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(false)
        }
    }
}

