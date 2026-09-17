package com.adrc95.rickyandmorty.framework.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun isSystemInDarkThemeCompat(): Boolean {
    val viewController = LocalUIViewController.current
    var style by remember(viewController) {
        mutableStateOf(viewController.traitCollection.userInterfaceStyle)
    }

    DisposableEffect(viewController) {
        val traitView = TraitObserverView { newStyle -> style = newStyle }
        viewController.view.addSubview(traitView)
        onDispose { traitView.removeFromSuperview() }
    }

    return style == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@ExportObjCClass
private class TraitObserverView(
    private val onTraitChanged: (UIUserInterfaceStyle) -> Unit
) : UIView(frame = CGRectZero.readValue()) {
    override fun traitCollectionDidChange(previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        onTraitChanged(traitCollection.userInterfaceStyle)
    }
}
