plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://www.xbaimiao.com/repository/maven-releases/") }
}

dependencies {
    implementation("org.javassist:javassist:3.28.0-GA")
    compileOnly("org.bukkit:yaml:1.16.1")
    compileOnly("paper:paper:1.16.5")
}

tasks.shadowJar {
    baseName = "BANOP"
    this.manifest {
        this.attributes(
            "Premain-Class" to "com.xbaimiao.bansetop.CoreInject"
        )
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}