pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val spotLessVersion : String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "com.diffplug.spotless" -> useVersion(spotLessVersion)
            }
        }
    }
}

rootProject.name = "authentication"

include(
    ":core-api",
    ":domain",
    ":infra",
    ":support"
)
