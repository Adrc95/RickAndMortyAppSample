import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.ktlint)
}

ktlint {
    version.set(libs.versions.ktlint.get())
    android.set(true)
}

android {
    namespace = "com.adrc95.rickyandmorty"
    compileSdk {
        version = release(libs.versions.android.compileSdk.get().toInt()) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.adrc95.rickyandmorty"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        aidl = false
        buildConfig = true
        shaders = false
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
