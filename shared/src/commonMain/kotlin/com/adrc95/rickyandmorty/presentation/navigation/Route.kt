package com.adrc95.rickyandmorty.presentation.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

sealed class Route : NavKey {
    @Serializable
    data object Home : NavKey

    @Serializable
    data class Detail(val id: Int) : NavKey

    @Serializable
    data object Settings : NavKey
}

val navSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Home::class, Route.Home.serializer())
            subclass(Route.Detail::class, Route.Detail.serializer())
            subclass(Route.Settings::class, Route.Settings.serializer())
        }
    }
}
