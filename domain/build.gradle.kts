plugins {
    java
}

dependencies {
    //Lombok
    compileOnly("org.projectlombok:lombok:${property("lombokVersion")}")
    annotationProcessor("org.projectlombok:lombok:${property("lombokVersion")}")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${property("assertJVersion")}")
    testImplementation("org.mockito:mockito-core:${property("mockitoVersion")}")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar{
    enabled = true
}
