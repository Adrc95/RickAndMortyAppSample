package com.adrc95.rickyandmorty.presentation.filter.model

import org.jetbrains.compose.resources.StringResource

data class FilterGroupDisplayModel(
    val id: String,
    val title: StringResource,
    val options: List<FilterOptionDisplayModel>
)
