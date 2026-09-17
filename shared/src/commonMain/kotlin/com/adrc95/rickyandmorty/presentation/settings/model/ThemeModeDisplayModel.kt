package com.adrc95.rickyandmorty.presentation.settings.model


import com.adrc95.rickyandmorty.domain.model.ThemeMode
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.dark_mode
import com.adrc95.rickyandmorty.generated.resources.icon_dark_mode
import com.adrc95.rickyandmorty.generated.resources.icon_light_mode
import com.adrc95.rickyandmorty.generated.resources.icon_system_mode
import com.adrc95.rickyandmorty.generated.resources.light_mode
import com.adrc95.rickyandmorty.generated.resources.system_mode

enum class ThemeModeDisplayModel(
    val text: StringResource,
    val icon: DrawableResource
) {
    LIGHT(
        Res.string.light_mode,
        Res.drawable.icon_light_mode
    ),
    DARK(
        Res.string.dark_mode,
        Res.drawable.icon_dark_mode
    ),
    SYSTEM(
        Res.string.system_mode,
        Res.drawable.icon_system_mode
    );

    companion object {
        fun from(theme: ThemeMode): ThemeModeDisplayModel = when (theme) {
            ThemeMode.LIGHT -> LIGHT
            ThemeMode.DARK -> DARK
            ThemeMode.SYSTEM -> SYSTEM
        }
    }
}
