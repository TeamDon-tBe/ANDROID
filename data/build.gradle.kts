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
    implementation(project(":data-remote"))

    // android
    implementation(libs.bundles.room)
    implementation(libs.paging)

    // Third Party
    implementation(libs.bundles.retrofit)
}
