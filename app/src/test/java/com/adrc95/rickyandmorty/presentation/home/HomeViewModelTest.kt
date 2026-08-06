package com.adrc95.rickyandmorty.presentation.home

import androidx.paging.PagingData
import app.cash.turbine.test
import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.filterGroup
import com.adrc95.rickyandmorty.domain.usecase.GetCharactersUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetFilterGroupsUseCase
import com.adrc95.rickyandmorty.domain.usecase.SearchCharactersUseCase
import com.adrc95.rickyandmorty.domain.usecase.ToggleFavouriteUseCase
import com.adrc95.rickyandmorty.presentation.builder.characterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.common.MainDispatcherRule
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import com.adrc95.rickyandmorty.testing.createPagingDataDiffer
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @MockK
    private lateinit var searchCharactersUseCase: SearchCharactersUseCase

    @MockK
    private lateinit var toggleFavouriteUseCase: ToggleFavouriteUseCase

    @MockK
    private lateinit var getFilterGroupsUseCase: GetFilterGroupsUseCase

    @Before
    fun `set up`() {
        MockKAnnotations.init(this)
        every { getFilterGroupsUseCase() } returns listOf(
            filterGroup { withId(GetFilterGroupsUseCase.SPECIES_GROUP_ID) },
            filterGroup { withId(GetFilterGroupsUseCase.GENDER_GROUP_ID) },
        )
    }

    private fun createViewModel() = HomeViewModel(
        getCharactersUseCase = getCharactersUseCase,
        searchCharactersUseCase = searchCharactersUseCase,
        toggleFavouriteUseCase = toggleFavouriteUseCase,
        getFilterGroupsUseCase = getFilterGroupsUseCase,
    )

    private fun mockCharacters() {
        every { getCharactersUseCase() } returns flowOf(PagingData.empty())
        every {
            searchCharactersUseCase(any(), any(), any(), any())
        } returns flowOf(PagingData.empty())
    }

    @Test
    fun `given viewmodel created when observing uiState then emits default state and filter groups`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()

            // When
            val viewModel = createViewModel()

            // Then
            viewModel.uiState.test {
                awaitItem()
                advanceUntilIdle()
                val state = viewModel.uiState.value
                assertEquals("", state.searchQuery)
                assertEquals(2, state.filterGroups.size)
                assertEquals(GetFilterGroupsUseCase.SPECIES_GROUP_ID, state.filterGroups[0].id)
                assertTrue(state.filters.species == null)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given empty query when observing characters then loads all characters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) { getCharactersUseCase() }
                verify(exactly = 0) {
                    searchCharactersUseCase(any(), any(), any(), any())
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given query shorter than three characters when observing characters then loads all characters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.onSearchQueryChange("ri")
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) { getCharactersUseCase()}
                verify(exactly = 0) {
                    searchCharactersUseCase(any(), any(), any(), any())
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given query with exactly three characters when observing characters then searches by name`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.onSearchQueryChange("abc")
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = "abc",
                        species = null,
                        gender = null,
                        status = null,
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given query with at least three characters when observing characters then searches by name`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.onSearchQueryChange("rick")
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null,
                    )
                }
                verify(exactly = 0) { getCharactersUseCase()}
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given filters selected when observing characters then searches with domain filters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.onFiltersChange(
                characterFiltersDisplayModel {
                    withSpecies(FilterOptionDisplayModel.Species.Human)
                    withGender(FilterOptionDisplayModel.Gender.Male)
                    withStatus(FilterOptionDisplayModel.Status.Alive)
                }
            )
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = null,
                        species = "Human",
                        gender = "Male",
                        status = "Alive",
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given filters changed when observing uiState then emits selected filters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()
            val filters = characterFiltersDisplayModel {
                withSpecies(FilterOptionDisplayModel.Species.Human)
            }

            // When
            viewModel.onFiltersChange(filters)

            // Then
            viewModel.uiState.test {
                assertEquals(filters, awaitItem().filters)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given query and filters selected when observing characters then searches with both`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()
            viewModel.onSearchQueryChange("rick")
            viewModel.onFiltersChange(
                characterFiltersDisplayModel {
                    withSpecies(FilterOptionDisplayModel.Species.Human)
                }
            )

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = "Human",
                        gender = null,
                        status = null,
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given short query and filters selected when observing characters then searches with null name`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()
            viewModel.onSearchQueryChange("ri")
            viewModel.onFiltersChange(
                characterFiltersDisplayModel {
                    withStatus(FilterOptionDisplayModel.Status.Alive)
                }
            )

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = null,
                        species = null,
                        gender = null,
                        status = "Alive",
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given several queries during debounce when observing characters then searches only with latest query`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()
                viewModel.onSearchQueryChange("ri")
                viewModel.onSearchQueryChange("rick")
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null,
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given same query emitted twice when observing characters then searches only once`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()
            viewModel.onSearchQueryChange("rick")

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()
                viewModel.onSearchQueryChange("rick")
                advanceTimeBy(300.milliseconds)

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null,
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given filters changed during debounce when observing characters then searches with latest filters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()
                viewModel.onFiltersChange(
                    characterFiltersDisplayModel {
                        withSpecies(FilterOptionDisplayModel.Species.Alien)
                    }
                )
                viewModel.onFiltersChange(
                    characterFiltersDisplayModel {
                        withSpecies(FilterOptionDisplayModel.Species.Human)
                    }
                )
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) {
                    searchCharactersUseCase(
                        name = null,
                        species = "Human",
                        gender = null,
                        status = null,
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given filters selected when filters are cleared then loads all characters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()
            viewModel.onFiltersChange(
                characterFiltersDisplayModel {
                    withSpecies(FilterOptionDisplayModel.Species.Human)
                }
            )

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()
                viewModel.onFiltersChange(characterFiltersDisplayModel())
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly = 1) { getCharactersUseCase() }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given characters loaded when observing characters then maps them to display models`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            val character = character {
                withId(1)
                withName("Rick Sanchez")
                withStatus("Dead")
            }
            every { getCharactersUseCase() } returns flowOf(PagingData.from(listOf(character)))
            every {
                searchCharactersUseCase(any(), any(), any(), any())
            } returns flowOf(PagingData.empty())
            val viewModel = createViewModel()
            val differ = createPagingDataDiffer<CharacterDisplayModel>(
                areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
            )

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                differ.submitData(awaitItem())
                advanceUntilIdle()

                // Then
                assertEquals(1, differ.snapshot().size)
                assertEquals(1, differ.snapshot()[0]!!.id)
                assertEquals("Rick Sanchez", differ.snapshot()[0]!!.name)
                assertEquals(CharacterStatusDisplayModel.DEAD, differ.snapshot()[0]!!.status)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given search query changed when observing uiState then emits query and filters`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.onSearchQueryChange("rick")

            // Then
            viewModel.uiState.test {
                assertEquals("rick", awaitItem().searchQuery)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given toggle favourite called then invokes toggleFavouriteUseCase`() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            coEvery { toggleFavouriteUseCase(1) } just Runs
            val viewModel = createViewModel()

            // When
            viewModel.onToggleFavourite(1)
            advanceUntilIdle()

            // Then
            coVerify { toggleFavouriteUseCase(1) }
        }

}
