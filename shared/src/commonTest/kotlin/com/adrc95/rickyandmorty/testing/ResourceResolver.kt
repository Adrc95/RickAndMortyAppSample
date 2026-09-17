package com.adrc95.rickyandmorty.testing

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

fun StringResource.resolve(): String = runBlocking { getString(this@resolve) }
