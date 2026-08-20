package com.adrc95.rickyandmorty.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.data.DataConstants.THEME_MODE_KEY
import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class SettingsRepositoryIntegrationTest : KoinTest {

    private val repository: SettingsRepository by inject()

    private val dataStore: DataStore<Preferences> by inject()

    @Before
    fun setUp() {
        runBlocking {
            dataStore.edit { it.clear() }
        }
    }

    @Test
    fun givenNoThemeMode_whenGetThemeMode_thenReturnsSystem() = runTest {
        assertEquals(ThemeMode.SYSTEM, repository.getThemeMode().first())
    }

    @Test
    fun givenLightMode_whenSetThemeMode_thenReturnsLight() = runTest {
        repository.setThemeMode(ThemeMode.LIGHT)

        assertEquals(ThemeMode.LIGHT, repository.getThemeMode().first())
    }

    @Test
    fun givenDarkMode_whenSetThemeMode_thenReturnsDark() = runTest {
        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, repository.getThemeMode().first())
    }

    @Test
    fun givenThemeMode_whenChangingMode_thenPersistsLatestValue() = runTest {
        repository.setThemeMode(ThemeMode.LIGHT)
        repository.setThemeMode(ThemeMode.DARK)

        assertEquals(ThemeMode.DARK, repository.getThemeMode().first())
    }

    @Test
    fun givenUnknownStoredValue_whenGetThemeMode_thenReturnsSystem() = runTest {
        val themeModeKey = stringPreferencesKey(THEME_MODE_KEY)
        dataStore.edit { preferences ->
            preferences[themeModeKey] = "unexpected"
        }

        assertEquals(ThemeMode.SYSTEM, repository.getThemeMode().first())
    }
}
