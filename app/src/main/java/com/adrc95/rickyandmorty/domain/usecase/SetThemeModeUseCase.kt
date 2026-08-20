package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class SetThemeModeUseCase(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(mode: ThemeMode) {
        settingsRepository.setThemeMode(mode)
    }
}
