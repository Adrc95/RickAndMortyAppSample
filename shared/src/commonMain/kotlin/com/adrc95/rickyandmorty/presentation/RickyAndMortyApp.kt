package com.adrc95.rickyandmorty.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.usecase.GetThemeModeUseCase
import com.adrc95.rickyandmorty.framework.ui.ConfigureStatusBar
import com.adrc95.rickyandmorty.framework.ui.isSystemInDarkThemeCompat
import com.adrc95.rickyandmorty.presentation.navigation.NavigationRoot
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import org.koin.compose.koinInject

@Composable
fun RickyAndMortyApp() {
    val getThemeMode = koinInject<GetThemeModeUseCase>()
    val themeModeFlow = remember(getThemeMode) { getThemeMode() }
    val themeMode by themeModeFlow.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkThemeCompat()
    }
    ConfigureStatusBar(darkTheme = darkTheme, isSystemTheme = themeMode == ThemeMode.SYSTEM)
    RickyAndMortyTheme(
        themeMode = themeMode
    ) {
        NavigationRoot()
    }
}
