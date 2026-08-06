package com.adrc95.rickyandmorty.presentation.settings

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.TestTags.BACK_BUTTON
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.extension.string
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun givenLightMode_whenScreenIsDisplayed_thenShowsSettingsAndThemeOptions() {
        setContent(themeMode = ThemeModeDisplayModel.LIGHT)
        composeTestRule.onNodeWithText(R.string.settings.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText(R.string.appearance.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText(ThemeModeDisplayModel.LIGHT.text.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText(ThemeModeDisplayModel.DARK.text.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText(ThemeModeDisplayModel.SYSTEM.text.string()).assertIsDisplayed()
        composeTestRule.onAllNodes(isSelectable().and(isSelected())).assertCountEquals(1)
    }

    @Test
    fun whenDarkThemeOptionIsClicked_thenEmitsDarkModeAndUpdatesSelection() {
        var selectedMode: ThemeModeDisplayModel? = null
        var currentMode by mutableStateOf(ThemeModeDisplayModel.SYSTEM)
        composeTestRule.setContent {
            RickyAndMortyTheme {
                SettingsScreen(
                    themeMode = currentMode,
                    onThemeModeSelected = {
                        selectedMode = it
                        currentMode = it
                    },
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText(ThemeModeDisplayModel.DARK.text.string()).performClick()
        assertEquals(ThemeModeDisplayModel.DARK, selectedMode)
        composeTestRule.onAllNodes(isSelectable().and(isSelected())).assertCountEquals(1)
    }

    @Test
    fun whenLightAndSystemOptionsAreClicked_thenEmitsSelectedModes() {
        val selectedModes = mutableListOf<ThemeModeDisplayModel>()

        composeTestRule.setContent {
            RickyAndMortyTheme {
                SettingsScreen(
                    themeMode = ThemeModeDisplayModel.SYSTEM,
                    onThemeModeSelected = { selectedModes += it },
                    onBack = {}
                )
            }
        }

        composeTestRule.onNodeWithText(ThemeModeDisplayModel.LIGHT.text.string()).performClick()
        composeTestRule.onNodeWithText(ThemeModeDisplayModel.SYSTEM.text.string()).performClick()

        assertEquals(
            listOf(ThemeModeDisplayModel.LIGHT, ThemeModeDisplayModel.SYSTEM),
            selectedModes
        )
    }

    @Test
    fun whenBackButtonIsClicked_thenEmitsBackCallback() {
        var backClicked = false
        setContent(
            onBack = { backClicked = true }
        )
        composeTestRule
            .onNodeWithTag(BACK_BUTTON)
            .performClick()
        assertTrue(backClicked)
    }

    private fun setContent(themeMode: ThemeModeDisplayModel = ThemeModeDisplayModel.SYSTEM, onBack: () -> Unit = {}) {
        composeTestRule.setContent {
            RickyAndMortyTheme {
                SettingsScreen(
                    themeMode = themeMode,
                    onThemeModeSelected = {},
                    onBack = onBack
                )
            }
        }
    }
}
