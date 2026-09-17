package com.adrc95.rickyandmorty.framework.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isSystemInDarkThemeCompat(): Boolean = isSystemInDarkTheme()
