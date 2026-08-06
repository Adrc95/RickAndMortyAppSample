package com.adrc95.rickyandmorty.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrc95.rickyandmorty.domain.usecase.GetThemeModeUseCase
import com.adrc95.rickyandmorty.domain.usecase.SetThemeModeUseCase
import com.adrc95.rickyandmorty.presentation.core.PresentationConstants.WHILE_SUBSCRIBED_TIMEOUT_MILLIS
import com.adrc95.rickyandmorty.presentation.settings.mapper.toDisplayModel
import com.adrc95.rickyandmorty.presentation.settings.mapper.toDomain
import com.adrc95.rickyandmorty.presentation.settings.model.ThemeModeDisplayModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getThemeModeUseCase: GetThemeModeUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase
) : ViewModel() {

    val themeMode: StateFlow<ThemeModeDisplayModel> =
        getThemeModeUseCase()
            .map { it.toDisplayModel() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(WHILE_SUBSCRIBED_TIMEOUT_MILLIS),
                initialValue = ThemeModeDisplayModel.SYSTEM
            )

    fun onThemeModeSelected(mode: ThemeModeDisplayModel) {
        viewModelScope.launch {
            setThemeModeUseCase(mode.toDomain())
        }
    }
}
