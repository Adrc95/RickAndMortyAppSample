package com.adrc95.rickyandmorty.data.repository

import com.adrc95.rickyandmorty.data.datasource.SettingsPreferenceDataSource
import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(binds = [SettingsRepository::class])
class SettingsRepositoryImpl(private val settingsDataSource: SettingsPreferenceDataSource) : SettingsRepository {

    override fun getThemeMode(): Flow<ThemeMode> = settingsDataSource.themeMode.map { ThemeMode.from(it) }

    override suspend fun setThemeMode(mode: ThemeMode) {
        settingsDataSource.setThemeMode(mode.value)
    }
}
