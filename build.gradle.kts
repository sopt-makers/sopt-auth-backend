import org.gradle.api.tasks.bundling.Zip
import org.springframework.boot.gradle.tasks.bundling.BootJar

/* =====================================================
 * Versions
 * ===================================================== */
val googleJavaFormatVersion = "1.18.1"
val jacksonCoreVersion = property("jacksonDataBindVersion").toString()
val okHttpVersion = property("okHttp3Version").toString()
val gsonVersion = property("gsonVersion").toString()
val bouncycastleVersion = property("bouncycastleVersion").toString()
val redissonVersion = property("redissonVersion").toString()
val javaContainerVersion = property("javaContainerVersion").toString()
val javaCore = property("javaCore").toString()
val javaEvents = property("javaEvents").toString()

/* =====================================================
 * Build Profile
 * ===================================================== */
val profile: String = project.findProperty("profile") as? String ?: "local"
val isLambdaProfile = profile == "dev" || profile == "prod"

println("▶ Build Profile = $profile (lambda=$isLambdaProfile)")

/* =====================================================
 * Plugins
 * ===================================================== */
plugins {
    java
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless")
}

/* =====================================================
 * Project Info
 * ===================================================== */
group = property("projectGroup").toString()
version = property("applicationVersion").toString()

repositories {
    mavenCentral()
}

/* =====================================================
 * Java
 * ===================================================== */
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

/* =====================================================
 * Dependencies
 * ===================================================== */
dependencies {

    /* ---------- Web ---------- */
    if (isLambdaProfile) {
        implementation("org.springframework.boot:spring-boot-starter-web") {
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
        }
    } else {
        implementation("org.springframework.boot:spring-boot-starter-web")
    }

    /* ---------- Spring Core ---------- */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.retry:spring-retry")

    /* ---------- Observability ---------- */
    implementation("io.micrometer:micrometer-registry-prometheus-simpleclient")

    /* ---------- Serialization / HTTP ---------- */
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonCoreVersion")
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")

    /* ---------- Crypto ---------- */
    implementation("org.bouncycastle:bcprov-jdk18on:$bouncycastleVersion")
    implementation("org.bouncycastle:bcpkix-jdk18on:$bouncycastleVersion")

    /* ---------- AWS SDK ---------- */
    implementation("software.amazon.awssdk:s3:2.20.26")

    /* ---------- AWS Lambda ---------- */
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:${javaContainerVersion}")
    implementation("com.amazonaws:aws-lambda-java-core:${javaCore}")
    implementation("com.amazonaws:aws-lambda-java-events:${javaEvents}")

    /* ---------- Cache ---------- */
    implementation("org.redisson:redisson-spring-boot-starter:${redissonVersion}")

    /* ---------- DB ---------- */
    runtimeOnly("org.postgresql:postgresql")

    /* ---------- Lombok ---------- */
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    /* ---------- Test ---------- */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2")
}

/* =====================================================
 * Spotless
 * ===================================================== */
spotless {
    java {
        target("**/*.java")
        googleJavaFormat(googleJavaFormatVersion)
        importOrder("sopt", "java", "javax", "jakarta", "org", "com")
        endWithNewline()
        removeUnusedImports()
    }
}

/* =====================================================
 * Test
 * ===================================================== */
tasks.withType<Test> {
    useJUnitPlatform()
}

/* =====================================================
 * BootJar / Jar
 * ===================================================== */
tasks.named<BootJar>("bootJar") {
    enabled = true
    archiveFileName.set("authentication.jar")
}

tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier.set("")
}

/* =====================================================
 * Lambda ZIP (Packaging Only)
 * ===================================================== */
tasks.register<Zip>("lambdaJar") {
    group = "distribution"
    description = "Build AWS Lambda deployment ZIP"

    dependsOn(tasks.named("jar"))

    archiveClassifier.set("lambda")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    isZip64 = true

    // Lambda 표준 구조: lib/
    into("lib") {
        from(tasks.named("jar"))
        from(configurations.runtimeClasspath) {
            exclude("org/apache/tomcat/embed/**")
            exclude("META-INF/*.SF")
            exclude("META-INF/*.DSA")
            exclude("META-INF/*.RSA")
            exclude("META-INF/MANIFEST.MF")
            exclude("**/module-info.class")
        }
    }
}