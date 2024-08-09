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
        // Mapbox
        maven {

            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")

            credentials.username = "mapbox"

            // Use the secret token stored in gradle.properties as the password

            credentials.password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN").get()

            authentication {  create<BasicAuthentication>("basic") }
        }
    }
}

rootProject.name = "My-Taxi-Test-Task"
include(":app")
 