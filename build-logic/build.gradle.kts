plugins {
    `kotlin-dsl`
}

group = "com.example.dontbe.buildlogic"

dependencies {
    compileOnly(libs.agp)
    compileOnly(libs.kotlin.gradleplugin)
}

gradlePlugin {
    plugins {
        create("android-application") {
            id = "com.example.dontbe.application"
            implementationClass = "com.example.dontbe.plugin.AndroidApplicationPlugin"
        }
        create("android-feature") {
            id = "com.example.dontbe.feature"
            implementationClass = "com.example.dontbe.plugin.AndroidFeaturePlugin"
        }
        create("android-kotlin") {
            id = "com.example.dontbe.kotlin"
            implementationClass = "com.example.dontbe.plugin.AndroidKotlinPlugin"
        }
        create("android-hilt") {
            id = "com.example.dontbe.hilt"
            implementationClass = "com.example.dontbe.plugin.AndroidHiltPlugin"
        }
        create("kotlin-serialization") {
            id = "com.example.dontbe.serialization"
            implementationClass = "com.example.dontbe.plugin.KotlinSerializationPlugin"
        }
        create("android-test") {
            id = "com.example.dontbe.test"
            implementationClass = "com.example.dontbe.plugin.AndroidTestPlugin"
        }
    }
}