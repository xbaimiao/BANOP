import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.5.31"
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.xbaimiao.com/repository/maven-releases/") }
}

dependencies {
    compileOnly("paper:paper:1.16.5")
    testCompileOnly("paper:paper:1.16.5")
    implementation("org.javassist:javassist:3.28.0-GA")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.shadowJar {
    baseName = "BANOP"
    this.manifest {
        this.attributes(
            "Premain-Class" to "com.xbaimiao.banop.Core"
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}