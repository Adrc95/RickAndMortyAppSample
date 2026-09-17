package com.adrc95.rickyandmorty.presentation.detail

import app.cash.turbine.test
import com.adrc95.rickyandmorty.presentation.common.MainDispatcherRule
import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.AppErrorException
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.usecase.GetCharacterByIdUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetEpisodesByIdsUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetLocationByIdUseCase
import com.adrc95.rickyandmorty.domain.usecase.IsCharacterFavouriteUseCase
import com.adrc95.rickyandmorty.domain.usecase.ToggleFavouriteUseCase
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.navigation.Route
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val mainDispatcherRule = MainDispatcherRule()

    private val getCharacterByIdUseCase = mock<GetCharacterByIdUseCase>()
    private val getLocationByIdUseCase = mock<GetLocationByIdUseCase>()
    private val getEpisodesByIdsUseCase = mock<GetEpisodesByIdsUseCase>()
    private val isCharacterFavouriteUseCase = mock<IsCharacterFavouriteUseCase>()
    private val toggleFavouriteUseCase = mock<ToggleFavouriteUseCase>()

    private val navKey = Route.Detail(id = 1)

    private fun createViewModel() = DetailViewModel(
        navKey = navKey,
        getCharacterByIdUseCase = getCharacterByIdUseCase,
        getLocationByIdUseCase = getLocationByIdUseCase,
        getEpisodesByIdsUseCase = getEpisodesByIdsUseCase,
        isCharacterFavouriteUseCase = isCharacterFavouriteUseCase,
        toggleFavouriteUseCase = toggleFavouriteUseCase
    )

    private fun mockSuccessfulCharacterResponse() {
        val character = character {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withOrigin(
                summaryLocation {
                    withId(1)
                    withName("Earth (C-137)")
                }
            )
            withLocation(
                summaryLocation {
                    withId(3)
                    withName("Citadel of Ricks")
                }
            )
            withEpisodeIds(listOf(1, 2))
        }
        every { getCharacterByIdUseCase(1) } returns flowOf(character)
        everySuspend { getLocationByIdUseCase(1, 1, isOrigin = true) } returns Result.Success(
            locationDetail {
                withId(1)
                withName("Earth (C-137)")
            }
        )
        everySuspend { getLocationByIdUseCase(1, 3, isOrigin = false) } returns Result.Success(
            locationDetail {
                withId(3)
                withName("Citadel of Ricks")
            }
        )
        everySuspend { getEpisodesByIdsUseCase(1, listOf(1, 2)) } returns Result.Success(
            listOf(
                episodeDetail {
                    withId(1)
                    withName("Pilot")
                },
                episodeDetail {
                    withId(2)
                    withName("Lawnmower Dog")
                }
            )
        )
        every { isCharacterFavouriteUseCase(1) } returns flowOf(false)
    }

    @BeforeTest
    fun set_up() {
        mainDispatcherRule.setUp()
    }

    @AfterTest
    fun tear_down() {
        mainDispatcherRule.tearDown()
    }

    @Test
    fun given_character_loads_when_observing_uiState_then_emits_loading_first() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockSuccessfulCharacterResponse()

            // When
            val viewModel = createViewModel()

            // Then
            val loading = viewModel.uiState.value
            assertTrue(loading.isLoading)
        }

    @Test
    fun given_character_loads_when_observing_uiState_then_emits_character() = runTest(mainDispatcherRule.scheduler) {
        // Given
        mockSuccessfulCharacterResponse()

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNotNull(state.character)
            assertEquals(1, state.character!!.id)
            assertEquals("Rick Sanchez", state.character.name)
            assertEquals(CharacterStatusDisplayModel.ALIVE, state.character.status)
            assertEquals("Human", state.character.species)
            assertEquals(false, state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_character_with_details_when_loading_then_combines_origin_location_and_episodes() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockSuccessfulCharacterResponse()

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals("Earth (C-137)", state.character!!.originDetail!!.name)
                assertEquals("Citadel of Ricks", state.character.locationDetail!!.name)
                assertEquals(2, state.character.episodeDetails.size)
                assertEquals("Pilot", state.character.episodeDetails[0].name)
                assertEquals("Lawnmower Dog", state.character.episodeDetails[1].name)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_character_is_favourite_when_loading_then_combines_favourite_state() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            everySuspend { getLocationByIdUseCase(any(), any(), any()) } returns Result.Success(null)
            everySuspend { getEpisodesByIdsUseCase(any(), any()) } returns Result.Success(emptyList())
            every { isCharacterFavouriteUseCase(1) } returns flowOf(true)

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(true, state.character!!.isFavourite)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_character_detail_fails_when_loading_then_emits_app_error() = runTest(mainDispatcherRule.scheduler) {
        // Given
        every { getCharacterByIdUseCase(1) } returns flow {
            throw AppErrorException(AppError.Connectivity)
        }
        every { isCharacterFavouriteUseCase(1) } returns flowOf(false)

        // When
        val viewModel = createViewModel()

        // Then
        viewModel.uiState.test {
            val errorState = awaitItem()
            assertNull(errorState.character)
            assertEquals(AppError.Connectivity, errorState.error)
            assertEquals(false, errorState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun given_character_detail_fails_with_unknown_error_when_loading_then_maps_to_app_error() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            every { getCharacterByIdUseCase(1) } returns flow {
                throw RuntimeException("Unexpected")
            }
            every { isCharacterFavouriteUseCase(1) } returns flowOf(false)

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                val errorState = awaitItem()
                assertNotNull(errorState.error)
                assertTrue(errorState.error is AppError.Unknown)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_location_fails_when_loading_character_then_keeps_character_with_null_details() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
                withEpisodeIds(listOf(1))
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            everySuspend { getLocationByIdUseCase(any(), any(), any()) } returns Result.Error(AppError.Connectivity)
            everySuspend { getEpisodesByIdsUseCase(any(), any()) } returns Result.Success(emptyList())
            every { isCharacterFavouriteUseCase(1) } returns flowOf(false)

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertNotNull(state.character)
                assertNull(state.character!!.originDetail)
                assertNull(state.character.locationDetail)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_episodes_fail_when_loading_character_then_keeps_character_with_empty_episodes() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
                withEpisodeIds(listOf(1))
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            everySuspend { getLocationByIdUseCase(any(), any(), any()) } returns Result.Success(null)
            everySuspend { getEpisodesByIdsUseCase(any(), any()) } returns Result.Error(AppError.Server(500))
            every { isCharacterFavouriteUseCase(1) } returns flowOf(false)

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                val state = awaitItem()
                assertNotNull(state.character)
                assertEquals(emptyList<Any>(), state.character!!.episodeDetails)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_toggle_favourite_called_then_invokes_toggleFavouriteUseCase() = runTest(mainDispatcherRule.scheduler) {
        // Given
        mockSuccessfulCharacterResponse()
        everySuspend { toggleFavouriteUseCase(1) } returns Unit

        val viewModel = createViewModel()

        // When
        viewModel.onToggleFavourite()

        // Then
        verifySuspend { toggleFavouriteUseCase(1) }
    }
}
