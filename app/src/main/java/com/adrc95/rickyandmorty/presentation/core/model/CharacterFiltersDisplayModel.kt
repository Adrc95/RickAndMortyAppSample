package com.adrc95.rickyandmorty.presentation.core.model

import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel

data class CharacterFiltersDisplayModel(
    val species: FilterOptionDisplayModel.Species? = null,
    val gender: FilterOptionDisplayModel.Gender? = null,
    val status: FilterOptionDisplayModel.Status? = null,
)