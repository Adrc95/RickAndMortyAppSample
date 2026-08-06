package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetThemeModeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<ThemeMode> = settingsRepository.getThemeMode()
}
