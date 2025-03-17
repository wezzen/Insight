dependencies {
    implementation(project(":controller"))
    implementation(project(":dto"))
    implementation(project(":model"))
    implementation(project(":repository"))
    implementation(project(":service"))
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}