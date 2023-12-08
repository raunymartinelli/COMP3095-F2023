rootProject.name = "notification-service"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "org.springframework.boot") {
                useVersion("3.2.0")
            }
        }
    }
}
