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

val archunitVersion = "${property("archunitVersion")}"
val fixtureMonkeyVersion = "${property("fixturemonkeyVersion")}"
val jacksonCoreVersion = "${property("jacksonDataBindVersion")}"
val okHttpVersion = "${property("okHttp3Version")}"
val gsonVersion = "${property("gsonVersion")}"
val bouncycastleVersion = "${property("bouncycastleVersion")}"
val jsonwebtokenVersion = "${property("jsonwebtokenVersion")}"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.retry:spring-retry")

	implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonCoreVersion}")

	implementation ("com.squareup.okhttp3:okhttp:${okHttpVersion}")
	implementation ("com.google.code.gson:gson:${gsonVersion}")

	implementation("org.bouncycastle:bcprov-jdk18on:${bouncycastleVersion}")
	implementation("org.bouncycastle:bcpkix-jdk18on:${bouncycastleVersion}")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("com.mysql:mysql-connector-j")
	annotationProcessor("org.projectlombok:lombok")

	implementation("io.jsonwebtoken:jjwt-api:${jsonwebtokenVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:${jsonwebtokenVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jsonwebtokenVersion}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.tngtech.archunit:archunit:${archunitVersion}")
	testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:${fixtureMonkeyVersion}")
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

// *-SNAPSHOT-plain.jar 생성 방지
tasks.getByName<Jar>("jar"){
	enabled=false
}
