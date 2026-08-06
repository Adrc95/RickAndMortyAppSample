package com.adrc95.rickyandmorty.presentation.settings

import app.cash.turbine.test
import com.adrc95.rickyandmorty.domain.model.ThemeMode
import com.adrc95.rickyandmorty.domain.usecase.GetThemeModeUseCase
import com.adrc95.rickyandmorty.domain.usecase.SetThemeModeUseCase
import com.adrc95.rickyandmorty.presentation.common.MainDispatcherRule
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getThemeModeUseCase: GetThemeModeUseCase

    @MockK
    private lateinit var setThemeModeUseCase: SetThemeModeUseCase

    private fun createViewModel() = SettingsViewModel(
        getThemeModeUseCase = getThemeModeUseCase,
        setThemeModeUseCase = setThemeModeUseCase
    )

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given theme mode is dark when observing themeMode then emits dark mode`() =
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
    fun `given no theme mode available when observing themeMode then emits system mode`() =
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
    fun `given light theme mode when observing themeMode then emits light mode`() =
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
    fun `given dark selected when selecting theme then calls setThemeModeUseCase with dark mode`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every {
                getThemeModeUseCase.invoke()
            } returns emptyFlow()

            coEvery {
                setThemeModeUseCase.invoke(any())
            } just Runs

            // When
            val settingsViewModel = createViewModel()

            settingsViewModel.onThemeModeSelected(
                ThemeModeDisplayModel.DARK
            )

            advanceUntilIdle()

            // Then
            coVerify {
                setThemeModeUseCase(ThemeMode.DARK)
            }
        }

    @Test
    fun `given light selected when selecting theme then calls setThemeModeUseCase with light mode`() = runTest {
        // Given
        every {
            getThemeModeUseCase.invoke()
        } returns emptyFlow()

        coEvery {
            setThemeModeUseCase.invoke(any())
        } just Runs

        // When
        val settingsViewModel = createViewModel()

        settingsViewModel.onThemeModeSelected(
            ThemeModeDisplayModel.LIGHT
        )

        advanceUntilIdle()

        // Then
        coVerify {
            setThemeModeUseCase(
                ThemeMode.LIGHT
            )
        }
    }
}
