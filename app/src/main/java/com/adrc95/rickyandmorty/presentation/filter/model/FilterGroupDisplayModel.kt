package com.adrc95.rickyandmorty.presentation.filter.model

import androidx.annotation.StringRes

data class FilterGroupDisplayModel(
    val id: String,
    @StringRes val title: Int,
    val options: List<FilterOptionDisplayModel>
)
