package com.adrc95.rickyandmorty.testing.extension

import androidx.test.platform.app.InstrumentationRegistry

fun Int.string(): String =
    InstrumentationRegistry.getInstrumentation()
        .targetContext
        .getString(this)

fun String.readJsonAsset(): String =
    InstrumentationRegistry.getInstrumentation()
        .context
        .assets
        .open(this)
        .bufferedReader()
        .use { it.readText() }
