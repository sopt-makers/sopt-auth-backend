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

/* =====================================================
 * Profile (BUILD TIME)
 * - 역할: 의존성 분기 (Tomcat 포함 여부)
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
        implementation("org.springframework.boot:spring-boot-starter-web")
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

    /* ---------- AWS ---------- */
    implementation("software.amazon.awssdk:s3:2.20.26")

    // Lambda runtime (dev/prod에서만 실제 사용)
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:2.1.5")
    implementation("com.amazonaws:aws-lambda-java-core:1.4.0")
    implementation("com.amazonaws:aws-lambda-java-events:3.16.1")

    /* ---------- Cache ---------- */
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

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
 * Git Hooks
 * ===================================================== */
tasks.register<Copy>("updateGitHooks") {
    from(".github/script/pre-commit")
    into(".git/hooks")
}

tasks.register<Exec>("makeGitHooksExecutable") {
    commandLine("chmod", "+x", ".git/hooks/pre-commit")
    dependsOn("updateGitHooks")
}

tasks.named("compileJava") {
    dependsOn("makeGitHooksExecutable")
}

/* =====================================================
 * BootJar / Jar
 * ===================================================== */
tasks.named<BootJar>("bootJar") {
    enabled = true
    archiveFileName.set("authentication.jar")
}

/**
 * Plain jar
 * - Lambda ZIP에서 사용
 */
tasks.named<Jar>("jar") {
    enabled = true
    archiveClassifier.set("")
}

/* =====================================================
 * Lambda ZIP (MANUAL)
 * ===================================================== */
tasks.register<Zip>("lambdaJar") {
    group = "distribution"
    description = "Build AWS Lambda deployment ZIP (manual)"

    dependsOn("jar")
    archiveClassifier.set("lambda")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    isZip64 = true

    // Lambda standard layout: lib/
    into("lib") {
        from(tasks.jar)
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