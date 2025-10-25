pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // <-- Add this line here
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Openhands"
include(":app")