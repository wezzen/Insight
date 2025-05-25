plugins {
    id("org.springframework.boot") version "3.5.0"
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