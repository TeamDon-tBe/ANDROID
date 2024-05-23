package com.teamdontbe.dontbe.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

internal fun Project.configureAndroidCommonPlugin() {
    val properties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    apply<AndroidKotlinPlugin>()
    apply<KotlinSerializationPlugin>()
    apply<GoogleFirebasePlugin>()
    with(plugins) {
        apply("kotlin-parcelize")
    }
    apply<AndroidHiltPlugin>()

    extensions.getByType<BaseExtension>().apply {
        defaultConfig {
            val dontbeBaseUrl = properties["dontbe.base.url"] as? String ?: ""
            val kakaoApiKey = properties["kakao.api.key"] as? String ?: ""
            val amplitudeApiKey = properties["amplitude.api.key"] as? String ?: ""

            manifestPlaceholders["dontbeBaseUrl"] = properties["dontbe.base.url"] as String
            manifestPlaceholders["kakaoApiKey"] = properties["kakao.api.key"] as String

            buildConfigField("String", "DONTBE_BASE_URL", "\"${dontbeBaseUrl}\"")
            buildConfigField("String", "KAKAO_APP_KEY", "\"${kakaoApiKey}\"")
            buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeApiKey}\"")
        }
        buildFeatures.apply {
            viewBinding = true
            buildConfig = true
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        "implementation"(libs.findLibrary("core.ktx").get())
        "implementation"(libs.findLibrary("appcompat").get())
        "implementation"(libs.findBundle("lifecycle").get())
        "implementation"(libs.findLibrary("material").get())
        "implementation"(libs.findLibrary("timber").get())
    }
}
