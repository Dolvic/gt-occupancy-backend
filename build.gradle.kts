plugins {
    id("org.jetbrains.kotlin.jvm") version "1.1.3"
    id("org.jetbrains.kotlin.plugin.spring") version "1.1.3"
    id("org.springframework.boot") version "1.5.4.RELEASE"
}

version = "1.0.0"

repositories {
    jcenter()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")

    compile("org.springframework.boot:spring-boot-starter")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-actuator")
}
