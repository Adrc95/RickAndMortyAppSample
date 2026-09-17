package com.adrc95.rickyandmorty.presentation.home

import androidx.paging.PagingData
import app.cash.turbine.test
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_GROUP_ID
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
import com.adrc95.rickyandmorty.testing.TestPagingDataDiffer
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.exactly
import dev.mokkery.verifySuspend
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val mainDispatcherRule = MainDispatcherRule()

    private val getCharactersUseCase = mock<GetCharactersUseCase>()
    private val searchCharactersUseCase = mock<SearchCharactersUseCase>()
    private val toggleFavouriteUseCase = mock<ToggleFavouriteUseCase>()
    private val getFilterGroupsUseCase = mock<GetFilterGroupsUseCase>()

    @BeforeTest
    fun set_up() {
        mainDispatcherRule.setUp()
        every { getFilterGroupsUseCase() } returns listOf(
            filterGroup { withId(SPECIES_GROUP_ID) },
            filterGroup { withId(GENDER_GROUP_ID) }
        )
    }

    @AfterTest
    fun tear_down() {
        mainDispatcherRule.tearDown()
    }

    private fun createViewModel() = HomeViewModel(
        getCharactersUseCase = getCharactersUseCase,
        searchCharactersUseCase = searchCharactersUseCase,
        toggleFavouriteUseCase = toggleFavouriteUseCase,
        getFilterGroupsUseCase = getFilterGroupsUseCase
    )

    private fun mockCharacters() {
        every { getCharactersUseCase() } returns flowOf(PagingData.empty())
        every {
            searchCharactersUseCase(any(), any(), any(), any())
        } returns flowOf(PagingData.empty())
    }

    @Test
    fun given_viewmodel_created_when_observing_uiState_then_emits_default_state_and_filter_groups() =
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
                assertEquals(SPECIES_GROUP_ID, state.filterGroups[0].id)
                assertTrue(state.filters.species == null)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_empty_query_when_observing_characters_then_loads_all_characters() =
        runTest(mainDispatcherRule.scheduler) {
            // Given
            mockCharacters()
            val viewModel = createViewModel()

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                awaitItem()

                // Then
                verify(exactly(1)) { getCharactersUseCase() }
                verify(exactly(0)) {
                    searchCharactersUseCase(any(), any(), any(), any())
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_query_shorter_than_three_characters_when_observing_characters_then_loads_all_characters() =
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
                verify(exactly(1)) { getCharactersUseCase() }
                verify(exactly(0)) {
                    searchCharactersUseCase(any(), any(), any(), any())
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_query_with_exactly_three_characters_when_observing_characters_then_searches_by_name() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = "abc",
                        species = null,
                        gender = null,
                        status = null
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_query_with_at_least_three_characters_when_observing_characters_then_searches_by_name() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null
                    )
                }
                verify(exactly(0)) { getCharactersUseCase() }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_filters_selected_when_observing_characters_then_searches_with_domain_filters() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = null,
                        species = "Human",
                        gender = "Male",
                        status = "Alive"
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_filters_changed_when_observing_uiState_then_emits_selected_filters() =
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
    fun given_query_and_filters_selected_when_observing_characters_then_searches_with_both() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = "Human",
                        gender = null,
                        status = null
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_short_query_and_filters_selected_when_observing_characters_then_searches_with_null_name() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = null,
                        species = null,
                        gender = null,
                        status = "Alive"
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_several_queries_during_debounce_when_observing_characters_then_searches_only_with_latest_query() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_same_query_emitted_twice_when_observing_characters_then_searches_only_once() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = "rick",
                        species = null,
                        gender = null,
                        status = null
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_filters_changed_during_debounce_when_observing_characters_then_searches_with_latest_filters() =
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
                verify(exactly(1)) {
                    searchCharactersUseCase(
                        name = null,
                        species = "Human",
                        gender = null,
                        status = null
                    )
                }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_filters_selected_when_filters_are_cleared_then_loads_all_characters() =
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
                verify(exactly(1)) { getCharactersUseCase() }
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_characters_loaded_when_observing_characters_then_maps_them_to_display_models() =
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
            val differ = TestPagingDataDiffer<CharacterDisplayModel>()

            // When
            viewModel.characters.test {
                advanceTimeBy(300.milliseconds)
                differ.collectFrom(awaitItem())
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
    fun given_search_query_changed_when_observing_uiState_then_emits_query_and_filters() =
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
    fun given_toggle_favourite_called_then_invokes_toggleFavouriteUseCase() = runTest(mainDispatcherRule.scheduler) {
        // Given
        mockCharacters()
        everySuspend { toggleFavouriteUseCase(1) } returns Unit
        val viewModel = createViewModel()

        // When
        viewModel.onToggleFavourite(1)
        advanceUntilIdle()

        // Then
        verifySuspend { toggleFavouriteUseCase(1) }
    }
}
