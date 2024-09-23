val googleJavaFormatVersion = "1.18.1"

plugins {
	java
	id("org.springframework.boot") apply false
	id("io.spring.dependency-management")
	id ("com.diffplug.spotless")
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

allprojects{
	group = "${property("projectGroup")}"
	version = "${property("applicationVersion")}"

	repositories{
		mavenCentral()
	}
}

subprojects{

	apply(plugin = "java")
	apply(plugin = "com.diffplug.spotless")

	dependencies{
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")
	}

	tasks.getByName("jar") {
		enabled = true
	}

	spotless{
		java {
			target("**/*.java")
			googleJavaFormat(googleJavaFormatVersion)
			importOrder("sopt", "java", "javax", "jakarta", "org", "com")
			indentWithTabs(2)
			endWithNewline()
			removeUnusedImports()
		}
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
