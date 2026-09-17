import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import de.jensklingenberg.ktorfit.gradle.ErrorCheckingMode

plugins {
    alias(libs.plugins.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.mokkery)
}

allOpen {
    annotation("com.adrc95.rickyandmorty.common.OpenForMokkery")
}

compose.resources {
    packageOfResClass = "com.adrc95.rickyandmorty.generated.resources"
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.adrc95.rickyandmorty.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }

        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.io.ktor.client.okhttp)
            implementation(libs.androidx.core.ktx)
        }
        iosMain.dependencies {
            implementation(libs.io.ktor.client.darwin)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.junit)
            implementation(libs.androidx.espresso.core)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.compose.ui.test.manifest)
            implementation(libs.mockwebserver)
            implementation(libs.koin.android)
            implementation(libs.koin.test)
            implementation(libs.io.ktor.client.okhttp)
            implementation(project.dependencies.platform(libs.koin.bom))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlin.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.compose.ui.test)
        }
        commonMain.dependencies {
            implementation(libs.io.ktor.serialization.kotlinx.json)
            implementation(libs.io.ktor.serialization.kotlinx.json)
            implementation(libs.io.ktor.client.serialization)
            implementation(libs.io.ktor.client.content.negotiation)
            implementation(libs.io.ktor.client.logging)
            implementation(libs.de.jensklingenberg.ktorfit.lib)
            implementation(libs.de.jensklingenberg.ktorfit.converters.response)
            implementation(libs.de.jensklingenberg.ktorfit.converters.flow)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.material3)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.annotations)
            implementation(libs.napier)
            implementation(libs.io.coil.compose)
            implementation(libs.io.coil.network.ktor)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.paging)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.navigation3)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
        }
    }
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

ktorfit {
    errorCheckingMode = ErrorCheckingMode.ERROR
    generateQualifiedTypeName = true
    compilerPluginVersion.set("2.3.5")
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    listOf(
        "kspAndroid",
        "kspIosSimulatorArm64",
        "kspIosArm64"
    ).forEach {
        add(it, libs.androidx.room.compiler)
    }
   androidRuntimeClasspath(libs.compose.uiTooling)
}

buildConfig {
    packageName("com.adrc95.rickyandmorty.shared")
    buildConfigField("API_URL", "https://rickandmortyapi.com/api/")
}

tasks.withType<Test>().configureEach {
    if (name == "testAndroidHostTest") {
        filter { excludeTestsMatching("*ScreenTest") }
    }
}
