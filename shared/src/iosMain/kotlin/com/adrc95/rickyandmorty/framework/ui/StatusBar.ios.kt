package com.adrc95.rickyandmorty.framework.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun ConfigureStatusBar(darkTheme: Boolean, isSystemTheme: Boolean) {
    val viewController = LocalUIViewController.current
    SideEffect {
        viewController.overrideUserInterfaceStyle = when {
            isSystemTheme -> UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            darkTheme -> UIUserInterfaceStyle.UIUserInterfaceStyleDark
            else -> UIUserInterfaceStyle.UIUserInterfaceStyleLight
        }
    }
}
