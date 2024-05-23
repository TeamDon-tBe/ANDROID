package com.teamdontbe.dontbe.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class GoogleFirebasePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        plugins.apply("com.google.gms.google-services")

        dependencies {
            "implementation"(libs.findBundle("firebase").get())
            "implementation"(platform(libs.findLibrary("firebase.bom").get()))
        }
    }
}
