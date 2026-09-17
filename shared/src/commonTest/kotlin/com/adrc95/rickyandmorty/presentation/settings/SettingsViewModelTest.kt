package com.adrc95.rickyandmorty.presentation.settings

import app.cash.turbine.test
import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.usecase.GetThemeModeUseCase
import com.adrc95.rickyandmorty.domain.usecase.SetThemeModeUseCase
import com.adrc95.rickyandmorty.presentation.common.MainDispatcherRule
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val mainDispatcherRule = MainDispatcherRule()

    private val getThemeModeUseCase = mock<GetThemeModeUseCase>()
    private val setThemeModeUseCase = mock<SetThemeModeUseCase>()

    private fun createViewModel() = SettingsViewModel(
        getThemeModeUseCase = getThemeModeUseCase,
        setThemeModeUseCase = setThemeModeUseCase
    )

    @BeforeTest
    fun set_up() {
        mainDispatcherRule.setUp()
    }

    @AfterTest
    fun tear_down() {
        mainDispatcherRule.tearDown()
    }

    @Test
    fun given_theme_mode_is_dark_when_observing_themeMode_then_emits_dark_mode() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase.invoke()
            } returns flowOf(
                ThemeMode.DARK
            )

            // When
            val settingsViewModel = createViewModel()
            settingsViewModel.themeMode.test {
                // Then
                assertEquals(
                    ThemeModeDisplayModel.DARK,
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_no_theme_mode_available_when_observing_themeMode_then_emits_system_mode() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase()
            } returns emptyFlow()

            // When
            val settingsViewModel = createViewModel()

            settingsViewModel.themeMode.test {
                // Then
                assertEquals(
                    ThemeModeDisplayModel.SYSTEM,
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_light_theme_mode_when_observing_themeMode_then_emits_light_mode() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase.invoke()
            } returns flowOf(
                ThemeMode.LIGHT
            )

            // When
            val settingsViewModel = createViewModel()

            settingsViewModel.themeMode.test {
                // Then
                assertEquals(
                    ThemeModeDisplayModel.LIGHT,
                    awaitItem()
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_dark_selected_when_selecting_theme_then_calls_setThemeModeUseCase_with_dark_mode() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase.invoke()
            } returns emptyFlow()

            everySuspend {
                setThemeModeUseCase.invoke(any())
            } returns Unit

            // When
            val settingsViewModel = createViewModel()

            settingsViewModel.onThemeModeSelected(
                ThemeModeDisplayModel.DARK
            )

            advanceUntilIdle()

            // Then
            verifySuspend {
                setThemeModeUseCase(ThemeMode.DARK)
            }
        }

    @Test
    fun given_light_selected_when_selecting_theme_then_calls_setThemeModeUseCase_with_light_mode() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase.invoke()
            } returns emptyFlow()

            everySuspend {
                setThemeModeUseCase.invoke(any())
            } returns Unit

            // When
            val settingsViewModel = createViewModel()

            settingsViewModel.onThemeModeSelected(
                ThemeModeDisplayModel.LIGHT
            )

            advanceUntilIdle()

            // Then
            verifySuspend {
                setThemeModeUseCase(
                    ThemeMode.LIGHT
                )
            }
        }
}
