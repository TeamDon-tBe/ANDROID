@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.feature"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":domain"))

    // AndroidX
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment.ktx)
    implementation(libs.splash.screen)

    // Third Party
    implementation(libs.coil.core)
    implementation(libs.bundles.retrofit)
    implementation(libs.kakao.login)
}