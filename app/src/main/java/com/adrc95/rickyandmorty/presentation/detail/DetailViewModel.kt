package com.adrc95.rickyandmorty.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.AppErrorException
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.usecase.GetCharacterByIdUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetEpisodesByIdsUseCase
import com.adrc95.rickyandmorty.domain.usecase.GetLocationByIdUseCase
import com.adrc95.rickyandmorty.domain.usecase.IsCharacterFavouriteUseCase
import com.adrc95.rickyandmorty.domain.usecase.ToggleFavouriteUseCase
import com.adrc95.rickyandmorty.presentation.core.mapper.toDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.navigation.Route
import com.adrc95.rickyandmorty.framework.toError
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel @AssistedInject constructor(
    @Assisted val navKey: Route.Detail,
    getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val getLocationByIdUseCase: GetLocationByIdUseCase,
    private val getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase,
    isCharacterFavouriteUseCase: IsCharacterFavouriteUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: Route.Detail): DetailViewModel
    }

    val uiState: StateFlow<UiState> =
        getCharacterByIdUseCase(navKey.id)
            .map { character ->
                coroutineScope {
                    val originDeferred = async { getLocationByIdUseCase(character.id, character.origin.id, isOrigin = true) }
                    val locationDeferred = async { getLocationByIdUseCase(character.id, character.location.id, isOrigin = false) }
                    val episodesDeferred = async { getEpisodesByIdsUseCase(character.id, character.episodeIds) }
                    val originDetail = when (val r = originDeferred.await()) { is Result.Success -> r.data else -> null }
                    val locationDetail = when (val r = locationDeferred.await()) { is Result.Success -> r.data else -> null }
                    val episodeDetails = when (val r = episodesDeferred.await()) { is Result.Success -> r.data else -> emptyList() }
                    character.toDisplayModel(originDetail, locationDetail, episodeDetails)
                }
            }
            .combine(isCharacterFavouriteUseCase(navKey.id)) { displayModel, isFavourite ->
                displayModel.copy(isFavourite = isFavourite)
            }
            .map { displayModel ->
                UiState(character = displayModel)
            }
            .onStart {
                emit(UiState(isLoading = true))
            }
            .catch { error ->
                val appError = when (error) {
                    is AppErrorException -> error.error
                    else -> error.toError()
                }
                emit(UiState(error = appError))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState(isLoading = true)
            )

    fun onToggleFavourite() {
        viewModelScope.launch {
            toggleFavouriteUseCase(navKey.id)
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val character: CharacterDisplayModel? = null,
        val error: AppError? = null,
    )
}
