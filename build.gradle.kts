plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")
}

repositories {
    mavenCentral()
    //mavenLocal()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

fun versionOf(name: String): String {
    return project.property("version.$name") as String
}

dependencies {
    implementation("io.ktor:ktor-server-core:${versionOf("ktor")}")
    implementation("io.ktor:ktor-serialization:${versionOf("ktor")}")
    implementation("io.ktor:ktor-html-builder:${versionOf("ktor")}")
    implementation("io.ktor:ktor-server-netty:${versionOf("ktor")}")
    implementation("ch.qos.logback:logback-classic:${versionOf("logback")}")

    testImplementation("io.ktor:ktor-server-tests:${versionOf("ktor")}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${versionOf("kotlin")}")

    implementation("com.h2database:h2:${versionOf("h2")}")
    implementation("org.jetbrains.exposed:exposed-core:${versionOf("exposed")}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${versionOf("exposed")}")
    implementation("com.zaxxer:HikariCP:${versionOf("hikariCp")}")

    implementation("org.kodein.di:kodein-di:${versionOf("kodein")}")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:${versionOf("kodein")}")
}