package com.adrc95.rickyandmorty

import androidx.compose.ui.window.ComposeUIViewController
import com.adrc95.rickyandmorty.di.initKoin
import com.adrc95.rickyandmorty.presentation.RickyAndMortyApp

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
    RickyAndMortyApp()
}
