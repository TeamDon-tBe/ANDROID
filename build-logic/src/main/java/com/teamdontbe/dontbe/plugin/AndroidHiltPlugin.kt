package com.teamdontbe.dontbe.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(plugins) {
            apply("kotlin-kapt")
            apply("com.google.dagger.hilt.android")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            "implementation"(libs.findLibrary("hilt").get())
            "kapt"(libs.findLibrary("hilt.compiler").get())
            "testImplementation"(libs.findLibrary("hilt.testing").get())
            "kaptTest"(libs.findLibrary("hilt.testing.compiler").get())
        }
    }
}