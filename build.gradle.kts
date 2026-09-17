// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.plugin.compose) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.mokkery) apply false
    alias(libs.plugins.kotlin.allopen) apply false
}
