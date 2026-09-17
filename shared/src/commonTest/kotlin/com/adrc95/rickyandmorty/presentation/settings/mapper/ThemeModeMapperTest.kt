package com.adrc95.rickyandmorty.presentation.settings.mapper

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import kotlin.test.Test
import kotlin.test.assertEquals

class ThemeModeMapperTest {

    @Test
    fun given_theme_mode_light_when_mapped_to_display_model_then_returns_light() {
        // Given
        val domain = ThemeMode.LIGHT

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.LIGHT, result)
    }

    @Test
    fun given_theme_mode_dark_when_mapped_to_display_model_then_returns_dark() {
        // Given
        val domain = ThemeMode.DARK

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.DARK, result)
    }

    @Test
    fun given_theme_mode_system_when_mapped_to_display_model_then_returns_system() {
        // Given
        val domain = ThemeMode.SYSTEM

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.SYSTEM, result)
    }

    @Test
    fun given_display_model_light_when_mapped_to_domain_then_returns_light() {
        // Given
        val display = ThemeModeDisplayModel.LIGHT

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.LIGHT, result)
    }

    @Test
    fun given_display_model_dark_when_mapped_to_domain_then_returns_dark() {
        // Given
        val display = ThemeModeDisplayModel.DARK

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.DARK, result)
    }

    @Test
    fun given_display_model_system_when_mapped_to_domain_then_returns_system() {
        // Given
        val display = ThemeModeDisplayModel.SYSTEM

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.SYSTEM, result)
    }
}
