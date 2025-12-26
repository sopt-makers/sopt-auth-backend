import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.gradle.api.tasks.bundling.Zip

val googleJavaFormatVersion = "1.18.1"

plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	id ("com.diffplug.spotless")
}

apply(plugin = "java")
apply(plugin = "com.diffplug.spotless")

group = "${property("projectGroup")}"

version = "${property("applicationVersion")}"
repositories {
	mavenCentral()
}


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

val jacksonCoreVersion = "${property("jacksonDataBindVersion")}"
val okHttpVersion = "${property("okHttp3Version")}"
val gsonVersion = "${property("gsonVersion")}"
val bouncycastleVersion = "${property("bouncycastleVersion")}"

dependencies {
	// Spring Web에서 Tomcat 제외 (Lambda에서는 불필요)
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.retry:spring-retry")

    implementation("io.micrometer:micrometer-registry-prometheus-simpleclient")

	implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonCoreVersion}")

	implementation ("com.squareup.okhttp3:okhttp:${okHttpVersion}")
	implementation ("com.google.code.gson:gson:${gsonVersion}")

	implementation("org.bouncycastle:bcprov-jdk18on:${bouncycastleVersion}")
	implementation("org.bouncycastle:bcpkix-jdk18on:${bouncycastleVersion}")

	implementation("software.amazon.awssdk:s3:2.20.26")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")

	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("com.github.ben-manes.caffeine:caffeine")

    // AWS Lambda Dependencies for JAR deployment
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:2.1.5")
    implementation("com.amazonaws:aws-lambda-java-core:1.4.0")
    implementation("com.amazonaws:aws-lambda-java-events:3.16.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")
}

spotless{
	java {
		target("**/*.java")
		googleJavaFormat(googleJavaFormatVersion)
		importOrder("sopt", "java", "javax", "jakarta", "org", "com")
		endWithNewline()
		removeUnusedImports()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

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

val jarName = "authentication.jar"
tasks.named<BootJar>("bootJar") {
	enabled = true
	archiveFileName.set(jarName)
}

// JAR 설정
tasks.named<Jar>("jar") {
	enabled = true
	archiveClassifier.set("")
}

// Lambda ZIP 빌드 설정
// 로컬 개발 시에는 필요 없으므로 build task에 포함하지 않음
// Lambda 배포가 필요할 때만: ./gradlew lambdaJar 실행
tasks.register<Zip>("lambdaJar") {
	dependsOn("jar")
	archiveClassifier.set("lambda")
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
	isZip64 = true // 대용량 ZIP 파일 지원

	// lib 디렉토리 구조로 패키징
	into("lib") {
		from(tasks.jar)
		from(configurations.runtimeClasspath) {
			// Lambda에서 불필요한 파일 제외
			exclude("org/apache/tomcat/embed/**")
			exclude("META-INF/*.SF")
			exclude("META-INF/*.DSA")
			exclude("META-INF/*.RSA")
			exclude("META-INF/MANIFEST.MF")
			exclude("**/module-info.class")
		}
	}
}

// build task에는 lambdaJar를 포함하지 않음
// - 로컬 개발/테스트: ./gradlew build (bootJar만 생성)
// - Lambda 배포: ./gradlew lambdaJar (Lambda ZIP 생성)
// - CI/CD에서 Lambda 배포가 필요하면: ./gradlew build lambdaJar

val profile: String = project.findProperty("profile") as? String ?: "test"
println("Build Profile: $profile")

tasks.register<Copy>("processProfileYaml") {
	from("src/main/resources/application-$profile.yaml")
	into("build/resources/main") // 빌드 시 사용할 리소스 경로
	rename { "application.yaml" } // 모든 프로파일 파일을 application.yaml로 변경
}

// processResources 작업 후에 실행되도록 의존성 추가
tasks.named("processResources") {
	dependsOn("processProfileYaml")
}