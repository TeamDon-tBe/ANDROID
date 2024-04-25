@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.teamdontbe.dontbe.feature")
    id("com.teamdontbe.dontbe.test")
}

android {
    namespace = "com.teamdontbe.feature"

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        debug {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    implementation(libs.paging)
    implementation(libs.swipe.refresh.layout)

    // Third Party
    implementation(libs.coil.core)
    implementation(libs.bundles.retrofit)
    implementation(libs.kakao.login)
    implementation(libs.lottie)
    implementation(libs.amplitude.ads)
    implementation(libs.google.play.services)
    implementation(libs.app.update)
}
