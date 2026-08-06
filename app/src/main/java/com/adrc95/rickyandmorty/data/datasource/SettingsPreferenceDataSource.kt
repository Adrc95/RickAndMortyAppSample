package com.adrc95.rickyandmorty.data.datasource

import kotlinx.coroutines.flow.Flow

interface SettingsPreferenceDataSource {
    val themeMode: Flow<String>
    suspend fun setThemeMode(mode: String)
}
