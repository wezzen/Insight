plugins {
    id("org.springframework.boot") version "3.4.4"
}

dependencies {
    implementation(project(":controller"))
    implementation(project(":dto"))
    implementation(project(":model"))
    implementation(project(":repository"))
    implementation(project(":service"))
    implementation(project(":security"))
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}