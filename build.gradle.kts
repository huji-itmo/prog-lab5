plugins {
    id("java")
    id ("java-library")
    id("io.freefair.lombok") version "8.6"
}

group = "org.huji"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {

    implementation("com.google.code.gson:gson:2.9.1")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks.withType<Jar>() {
    manifest {
        attributes["Main-Class"] = "Main"
    }
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "17"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.compileJava{
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}