@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.application")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.dontbe"

    configurations {
        implementation {
            exclude(group = "org.jetbrains", module = "annotations")
        }
    }

    defaultConfig {
        applicationId = "com.teamdontbe.dontbe"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":feature"))
    implementation(project(":core-ui"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":data-local"))
    implementation(project(":data-remote"))

    // AndroidX
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment.ktx)
    implementation(libs.splash.screen)
    implementation(libs.shared.security)

    // Third Party
    implementation(libs.coil.core)
    implementation(libs.bundles.retrofit)
    implementation(libs.kakao.login)
    implementation(libs.amplitude)
    implementation("com.google.firebase:firebase-messaging-ktx:24.0.0")
}
