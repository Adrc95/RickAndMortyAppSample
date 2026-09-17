package com.adrc95.rickyandmorty.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.adrc95.rickyandmorty.presentation.core.TestTags.BACK_BUTTON
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.resolve
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.appearance
import com.adrc95.rickyandmorty.generated.resources.settings

@OptIn(ExperimentalTestApi::class)
class SettingsScreenTest {

    @Test
    fun givenLightMode_whenScreenIsDisplayed_thenShowsSettingsAndThemeOptions() = runComposeUiTest {
        showSettings(themeMode = ThemeModeDisplayModel.LIGHT)
        onNodeWithText(Res.string.settings.resolve()).assertIsDisplayed()
        onNodeWithText(Res.string.appearance.resolve()).assertIsDisplayed()
        onNodeWithText(ThemeModeDisplayModel.LIGHT.text.resolve()).assertIsDisplayed()
        onNodeWithText(ThemeModeDisplayModel.DARK.text.resolve()).assertIsDisplayed()
        onNodeWithText(ThemeModeDisplayModel.SYSTEM.text.resolve()).assertIsDisplayed()
        onAllNodes(isSelectable().and(isSelected())).assertCountEquals(1)
    }

    @Test
    fun whenDarkThemeOptionIsClicked_thenEmitsDarkModeAndUpdatesSelection() = runComposeUiTest {
        var selectedMode: ThemeModeDisplayModel? = null
        var currentMode by mutableStateOf(ThemeModeDisplayModel.SYSTEM)
        setContent {
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

        onNodeWithText(ThemeModeDisplayModel.DARK.text.resolve()).performClick()
        assertEquals(ThemeModeDisplayModel.DARK, selectedMode)
        onAllNodes(isSelectable().and(isSelected())).assertCountEquals(1)
    }

    @Test
    fun whenLightAndSystemOptionsAreClicked_thenEmitsSelectedModes() = runComposeUiTest {
        val selectedModes = mutableListOf<ThemeModeDisplayModel>()

        setContent {
            RickyAndMortyTheme {
                SettingsScreen(
                    themeMode = ThemeModeDisplayModel.SYSTEM,
                    onThemeModeSelected = { selectedModes += it },
                    onBack = {}
                )
            }
        }

        onNodeWithText(ThemeModeDisplayModel.LIGHT.text.resolve()).performClick()
        onNodeWithText(ThemeModeDisplayModel.SYSTEM.text.resolve()).performClick()

        assertEquals(
            listOf(ThemeModeDisplayModel.LIGHT, ThemeModeDisplayModel.SYSTEM),
            selectedModes
        )
    }

    @Test
    fun whenBackButtonIsClicked_thenEmitsBackCallback() = runComposeUiTest {
        var backClicked = false
        showSettings(onBack = { backClicked = true })
        onNodeWithTag(BACK_BUTTON).performClick()
        assertTrue(backClicked)
    }

    private fun ComposeUiTest.showSettings(
        themeMode: ThemeModeDisplayModel = ThemeModeDisplayModel.SYSTEM,
        onBack: () -> Unit = {}
    ) {
        setContent {
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
