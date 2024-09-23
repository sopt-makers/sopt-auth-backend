plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":infra"))
    implementation(project(":support"))

    //Spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    //Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
