package com.adrc95.rickyandmorty.presentation.settings.mapper

import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeMapperTest {

    @Test
    fun `given theme mode light when mapped to display model then returns light`() {
        // Given
        val domain = ThemeMode.LIGHT

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.LIGHT, result)
    }

    @Test
    fun `given theme mode dark when mapped to display model then returns dark`() {
        // Given
        val domain = ThemeMode.DARK

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.DARK, result)
    }

    @Test
    fun `given theme mode system when mapped to display model then returns system`() {
        // Given
        val domain = ThemeMode.SYSTEM

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(ThemeModeDisplayModel.SYSTEM, result)
    }

    @Test
    fun `given display model light when mapped to domain then returns light`() {
        // Given
        val display = ThemeModeDisplayModel.LIGHT

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.LIGHT, result)
    }

    @Test
    fun `given display model dark when mapped to domain then returns dark`() {
        // Given
        val display = ThemeModeDisplayModel.DARK

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.DARK, result)
    }

    @Test
    fun `given display model system when mapped to domain then returns system`() {
        // Given
        val display = ThemeModeDisplayModel.SYSTEM

        // When
        val result = display.toDomain()

        // Then
        assertEquals(ThemeMode.SYSTEM, result)
    }
}
