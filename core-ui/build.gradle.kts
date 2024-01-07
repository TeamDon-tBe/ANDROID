@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
}

android {
    namespace = "com.teamdontbe.core_ui"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
}