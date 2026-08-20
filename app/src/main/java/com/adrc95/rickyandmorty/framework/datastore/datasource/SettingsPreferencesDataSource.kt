package com.adrc95.rickyandmorty.framework.datastore.datasource

import com.adrc95.rickyandmorty.data.DataConstants.THEME_MODE_DEFAULT
import com.adrc95.rickyandmorty.data.DataConstants.THEME_MODE_KEY
import com.adrc95.rickyandmorty.data.datasource.SettingsPreferenceDataSource
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single(binds = [SettingsPreferenceDataSource::class])
class SettingsPreferencesDataSource(private val preferences: DataStorePreferencesDataSource) :
    SettingsPreferenceDataSource {

    override val themeMode: Flow<String> =
        preferences.getFlow(THEME_MODE_KEY, THEME_MODE_DEFAULT)

    override suspend fun setThemeMode(mode: String) {
        preferences.set(THEME_MODE_KEY, mode)
    }
}
