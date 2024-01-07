pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
rootProject.name = "DontBe"
include(":app")
include(":core-ui")
include(":data-local")
include(":data-remote")
include(":data")
include(":domain")
include(":feature")
