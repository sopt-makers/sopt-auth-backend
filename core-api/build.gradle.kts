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
    implementation ("org.springframework.cloud:spring-cloud-starter-openfeign:${property("springOpenFeignVersion")}")

    //Test
    val restAssuredVersion = "${property("restassuredVersion")}"
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
