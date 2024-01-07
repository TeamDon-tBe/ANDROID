@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.data_local"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    //android
    implementation(libs.bundles.room)
}