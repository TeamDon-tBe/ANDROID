plugins {
    `kotlin-dsl`
}

group = "com.teamdontbe.dontbe.buildlogic"

dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "com.teamdontbe.dontbe.application"
            implementationClass = "com.teamdontbe.dontbe.plugin.AndroidApplicationPlugin"
        }
        create("android-feature") {
            id = "com.teamdontbe.dontbe.feature"
            implementationClass = "com.teamdontbe.dontbe.plugin.AndroidFeaturePlugin"
        }
        create("android-kotlin") {
            id = "com.teamdontbe.dontbe.kotlin"
            implementationClass = "com.teamdontbe.dontbe.plugin.AndroidKotlinPlugin"
        }
        create("android-hilt") {
            id = "com.teamdontbe.dontbe.hilt"
            implementationClass = "com.teamdontbe.dontbe.plugin.AndroidHiltPlugin"
        }
        create("kotlin-serialization") {
            id = "com.teamdontbe.dontbe.serialization"
            implementationClass = "com.teamdontbe.dontbe.plugin.KotlinSerializationPlugin"
        }
        create("android-test") {
            id = "com.teamdontbe.dontbe.test"
            implementationClass = "com.teamdontbe.dontbe.plugin.AndroidTestPlugin"
        }
    }
}