package com.adrc95.rickyandmorty.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Route: NavKey {
    @Serializable
    data object Home : NavKey

    @Serializable
    data class Detail(val id: Int) : NavKey

    @Serializable
    data object Settings : NavKey
}