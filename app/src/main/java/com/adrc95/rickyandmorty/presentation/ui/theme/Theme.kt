package com.adrc95.rickyandmorty.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.adrc95.rickyandmorty.domain.model.ThemeMode

val DarkColorScheme = darkColorScheme(
    primary = BlueGray300,
    onPrimary = Navy900,
    primaryContainer = Navy800,
    onPrimaryContainer = Blue50,
    secondary = Cyan100,
    onSecondary = Cyan950,
    secondaryContainer = Cyan800,
    onSecondaryContainer = Cyan50,
    tertiary = BlueGray200,
    onTertiary = Navy950,
    tertiaryContainer = Navy700,
    onTertiaryContainer = Blue100,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = Slate800,
    onSurfaceVariant = DarkTextSecondary,
    outline = Slate600,
    outlineVariant = DarkBorder,
    surfaceTint = BlueGray300,
    surfaceContainerLowest = DarkSurfaceLowest,
    surfaceContainerLow = Slate900,
    surfaceContainer = Slate800,
    surfaceContainerHigh = Slate700,
    surfaceContainerHighest = Slate600,
    inverseSurface = Slate100,
    inverseOnSurface = Slate900,
    error = Red200,
    onError = Red950,
    errorContainer = Red900,
    onErrorContainer = Red100
)

val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Navy900,
    onPrimaryContainer = BlueGray500,
    inversePrimary = BlueGray300,
    secondary = Cyan700,
    onSecondary = White,
    secondaryContainer = Cyan200,
    onSecondaryContainer = Cyan800,
    tertiary = Black,
    onTertiary = White,
    tertiaryContainer = Navy950,
    onTertiaryContainer = BlueGray600,
    background = Slate50,
    onBackground = Slate950,
    surface = Slate50,
    onSurface = Slate950,
    surfaceVariant = Slate400,
    onSurfaceVariant = Slate800,
    surfaceTint = Slate700,
    surfaceDim = Neutral100,
    surfaceBright = Slate50,
    surfaceContainerLowest = White,
    surfaceContainerLow = Slate100,
    surfaceContainer = Slate200,
    surfaceContainerHigh = Slate300,
    surfaceContainerHighest = Slate400,
    inverseSurface = Slate900,
    inverseOnSurface = Neutral50,
    outline = Slate600,
    outlineVariant = Slate500,
    error = Red700,
    onError = White,
    errorContainer = Red100,
    onErrorContainer = Red900,
    primaryFixed = Blue50,
    primaryFixedDim = BlueGray300,
    onPrimaryFixed = Navy900,
    onPrimaryFixedVariant = Navy800,
    secondaryFixed = Cyan50,
    secondaryFixedDim = Cyan100,
    onSecondaryFixed = Cyan950,
    onSecondaryFixedVariant = Cyan800,
    tertiaryFixed = Blue100,
    tertiaryFixedDim = BlueGray200,
    onTertiaryFixed = Navy950,
    onTertiaryFixedVariant = Navy700
)

@Composable
fun RickyAndMortyTheme(themeMode: ThemeMode = ThemeMode.SYSTEM, content: @Composable () -> Unit) {
    val isDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = if (isDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
