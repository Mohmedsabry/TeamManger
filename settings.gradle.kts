import java.net.URI

include(":domain")


include(":data")


include(":data")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            this.url = URI("https://jitpack.io")
        }
        maven { url = URI("https://maven.google.com") }
    }
}

rootProject.name = "TeamManger"
include(":app")
 