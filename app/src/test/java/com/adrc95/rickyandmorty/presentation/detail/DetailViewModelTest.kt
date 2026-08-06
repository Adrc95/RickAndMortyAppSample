package com.adrc95.rickyandmorty.presentation.detail

import app.cash.turbine.test
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
import com.adrc95.rickyandmorty.presentation.common.MainDispatcherRule
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.navigation.Route
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase

    @MockK
    private lateinit var getLocationByIdUseCase: GetLocationByIdUseCase

    @MockK
    private lateinit var getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase

    @MockK
    private lateinit var isCharacterFavouriteUseCase: IsCharacterFavouriteUseCase

    @MockK
    private lateinit var toggleFavouriteUseCase: ToggleFavouriteUseCase

    private val navKey = Route.Detail(id = 1)

    private fun createViewModel() = DetailViewModel(
        navKey = navKey,
        getCharacterByIdUseCase = getCharacterByIdUseCase,
        getLocationByIdUseCase = getLocationByIdUseCase,
        getEpisodesByIdsUseCase = getEpisodesByIdsUseCase,
        isCharacterFavouriteUseCase = isCharacterFavouriteUseCase,
        toggleFavouriteUseCase = toggleFavouriteUseCase,
    )

    private fun mockSuccessfulCharacterResponse() {
        val character = character {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withOrigin(summaryLocation { withId(1); withName("Earth (C-137)") })
            withLocation(summaryLocation { withId(3); withName("Citadel of Ricks") })
            withEpisodeIds(listOf(1, 2))
        }
        every { getCharacterByIdUseCase(1) } returns flowOf(character)
        coEvery { getLocationByIdUseCase(1, 1, isOrigin = true) } returns Result.Success(
            locationDetail { withId(1); withName("Earth (C-137)") }
        )
        coEvery { getLocationByIdUseCase(1, 3, isOrigin = false) } returns Result.Success(
            locationDetail { withId(3); withName("Citadel of Ricks") }
        )
        coEvery { getEpisodesByIdsUseCase(1, listOf(1, 2)) } returns Result.Success(
            listOf(
                episodeDetail { withId(1); withName("Pilot") },
                episodeDetail { withId(2); withName("Lawnmower Dog") },
            )
        )
        every { isCharacterFavouriteUseCase(1) } returns flowOf(false)
    }

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given character loads when observing uiState then emits loading first`() =
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
    fun `given character loads when observing uiState then emits character`() =
        runTest(mainDispatcherRule.scheduler) {
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
    fun `given character with details when loading then combines origin location and episodes`() =
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
    fun `given character is favourite when loading then combines favourite state`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            coEvery { getLocationByIdUseCase(any(), any(), any()) } returns Result.Success(null)
            coEvery { getEpisodesByIdsUseCase(any(), any()) } returns Result.Success(emptyList())
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
    fun `given character detail fails when loading then emits app error`() =
        runTest(mainDispatcherRule.scheduler) {
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
    fun `given character detail fails with unknown error when loading then maps to app error`() =
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
    fun `given location fails when loading character then keeps character with null details`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
                withEpisodeIds(listOf(1))
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            coEvery { getLocationByIdUseCase(any(), any(), any()) } returns Result.Error(AppError.Connectivity)
            coEvery { getEpisodesByIdsUseCase(any(), any()) } returns Result.Success(emptyList())
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
    fun `given episodes fail when loading character then keeps character with empty episodes`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withOrigin(summaryLocation { withId(1) })
                withLocation(summaryLocation { withId(3) })
                withEpisodeIds(listOf(1))
            }
            every { getCharacterByIdUseCase(1) } returns flowOf(character)
            coEvery { getLocationByIdUseCase(any(), any(), any()) } returns Result.Success(null)
            coEvery { getEpisodesByIdsUseCase(any(), any()) } returns Result.Error(AppError.Server(500))
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
    fun `given toggle favourite called then invokes toggleFavouriteUseCase`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockSuccessfulCharacterResponse()
            coEvery { toggleFavouriteUseCase(1) } just Runs

            val viewModel = createViewModel()

            // When
            viewModel.onToggleFavourite()

            // Then
            coVerify { toggleFavouriteUseCase(1) }
        }
}
