package com.adrc95.rickyandmorty.domain.repository

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeSettingsRepository : SettingsRepository {

    private val themeMode = MutableStateFlow(ThemeMode.SYSTEM)

    override fun getThemeMode(): Flow<ThemeMode> = themeMode.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        themeMode.value = mode
    }

    fun setThemeModeValue(mode: ThemeMode) {
        themeMode.value = mode
    }
}
