package com.adrc95.rickyandmorty.presentation.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.model.ThemeMode

enum class ThemeModeDisplayModel(@StringRes val text: Int, @DrawableRes val icon: Int) {
    LIGHT(
        R.string.light_mode,
        R.drawable.icon_light_mode
    ),
    DARK(
        R.string.dark_mode,
        R.drawable.icon_dark_mode
    ),
    SYSTEM(
        R.string.system_mode,
        R.drawable.icon_system_mode
    );

    companion object {
        fun from(theme: ThemeMode): ThemeModeDisplayModel = when (theme) {
            ThemeMode.LIGHT -> LIGHT
            ThemeMode.DARK -> DARK
            ThemeMode.SYSTEM -> SYSTEM
        }
    }
}
