plugins {
    java
    id("org.springframework.boot") apply true
    id("io.spring.dependency-management") apply true
}

dependencies {
    implementation(project(":domain"))

    //spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.cloud:spring-cloud-starter-openfeign:${property("springOpenFeignVersion")}")

    //db
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")


    //test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar{
    enabled = false
}
