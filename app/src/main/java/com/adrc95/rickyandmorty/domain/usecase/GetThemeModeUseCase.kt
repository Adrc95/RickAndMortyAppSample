package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetThemeModeUseCase(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<ThemeMode> = settingsRepository.getThemeMode()
}
