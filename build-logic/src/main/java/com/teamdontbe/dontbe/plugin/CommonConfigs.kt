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
            val kakaoApiKey = properties["kakao.api.key"] as? String ?: ""
            val amplitudeApiKey = properties["amplitude.api.key"] as? String ?: ""

            manifestPlaceholders["kakaoApiKey"] = properties["kakao.api.key"] as String

            buildConfigField("String", "KAKAO_APP_KEY", "\"${kakaoApiKey}\"")
            buildConfigField("String", "AMPLITUDE_API_KEY", "\"${amplitudeApiKey}\"")
        }

        buildTypes {
            getByName("debug") {
                val dontbeDevBaseUrl = properties["dontbe.dev.base.url"] as? String ?: ""
                buildConfigField("String", "DONTBE_BASE_URL", "\"${dontbeDevBaseUrl}\"")
                manifestPlaceholders["dontbeBaseUrl"] = properties["dontbe.dev.base.url"] as String
            }
            getByName("release") {
                val dontbeRelBaseUrl = properties["dontbe.rel.base.url"] as? String ?: ""
                buildConfigField("String", "DONTBE_BASE_URL", "\"${dontbeRelBaseUrl}\"")
                manifestPlaceholders["dontbeBaseUrl"] = properties["dontbe.rel.base.url"] as String
            }
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
