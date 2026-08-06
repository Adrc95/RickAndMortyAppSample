package com.adrc95.rickyandmorty.presentation.settings.mapper

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel.DARK
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel.LIGHT
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel.SYSTEM

fun ThemeMode.toDisplayModel(): ThemeModeDisplayModel = ThemeModeDisplayModel.from(this)

fun ThemeModeDisplayModel.toDomain(): ThemeMode = when(this) {
    LIGHT -> ThemeMode.LIGHT
    DARK -> ThemeMode.DARK
    SYSTEM -> ThemeMode.SYSTEM
}