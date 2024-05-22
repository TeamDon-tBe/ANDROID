@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.data"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))

    // android
    implementation(libs.bundles.room)
    implementation(libs.paging)
    implementation(libs.androidx.exifinterface)

    // Third Party
    implementation(libs.bundles.retrofit)
}
