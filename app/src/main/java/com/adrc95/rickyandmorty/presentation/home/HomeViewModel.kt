package com.adrc95.rickyandmorty.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.usecase.GetCharactersUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetFilterGroupsUseCase
import com.adrc95.rickyandmorty.domain.usecase.SearchCharactersUseCase
import com.adrc95.rickyandmorty.domain.usecase.ToggleFavouriteUseCase
import com.adrc95.rickyandmorty.presentation.filter.mapper.toDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.mapper.toDomain
import com.adrc95.rickyandmorty.presentation.filter.model.FilterGroupDisplayModel
import com.adrc95.rickyandmorty.presentation.core.mapper.toDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val searchCharactersUseCase: SearchCharactersUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    getFilterGroupsUseCase: GetFilterGroupsUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val filters = MutableStateFlow(CharacterFiltersDisplayModel())

    private val filterGroups: List<FilterGroupDisplayModel> = getFilterGroupsUseCase()
            .map { group -> group.toDisplayModel() }

    val uiState: StateFlow<UiState> =
        combine(
            searchQuery,
            filters,
        ) { query, activeFilters ->
            UiState(
                searchQuery = query,
                filters = activeFilters,
                filterGroups = filterGroups,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(),
        )

    val characters: Flow<PagingData<CharacterDisplayModel>> =
        uiState
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flatMapLatest { state ->
                val hasFilters =
                    state.filters.species != null ||
                            state.filters.gender != null ||
                            state.filters.status != null
                if (state.searchQuery.length < MIN_SEARCH_LENGTH && !hasFilters) {
                    getCharactersUseCase()

                } else {
                    searchCharactersUseCase(
                        name = state.searchQuery.takeIf {
                            it.length >= MIN_SEARCH_LENGTH
                        },
                        species = state.filters.species?.toDomain(),
                        gender = state.filters.gender?.toDomain(),
                        status = state.filters.status?.toDomain(),
                    )
                }
            }
            .map { pagingData ->
                pagingData.map { it.toDisplayModel() }
            }
            .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }


    fun onFiltersChange(
        newFilters: CharacterFiltersDisplayModel
    ) {
        filters.value = newFilters
    }

    fun onToggleFavourite(characterId: Int) {
        viewModelScope.launch {
            toggleFavouriteUseCase(characterId)
        }
    }

    data class UiState(
        val searchQuery: String = "",
        val filters: CharacterFiltersDisplayModel = CharacterFiltersDisplayModel(),
        val filterGroups: List<FilterGroupDisplayModel> = emptyList(),
        val error: AppError? = null,
    )

    private companion object {
        const val MIN_SEARCH_LENGTH = 3
    }
}
