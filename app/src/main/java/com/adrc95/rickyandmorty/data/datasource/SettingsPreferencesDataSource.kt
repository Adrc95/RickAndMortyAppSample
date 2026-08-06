package com.adrc95.rickyandmorty.data.datasource

import com.adrc95.rickyandmorty.data.DataConstants.THEME_MODE_DEFAULT
import com.adrc95.rickyandmorty.data.DataConstants.THEME_MODE_KEY
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SettingsPreferencesDataSource @Inject constructor(private val preferences: DataStorePreferencesDataSource) :
    SettingsPreferenceDataSource {

    override val themeMode: Flow<String> =
        preferences.getFlow(THEME_MODE_KEY, THEME_MODE_DEFAULT)

    override suspend fun setThemeMode(mode: String) {
        preferences.set(THEME_MODE_KEY, mode)
    }
}
