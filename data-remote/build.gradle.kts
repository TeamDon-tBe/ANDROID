@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.data_remote"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    //android
    implementation(libs.paging)

    // Third Party
    implementation(libs.bundles.retrofit)
}