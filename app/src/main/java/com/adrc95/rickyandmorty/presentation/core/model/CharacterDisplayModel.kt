package com.adrc95.rickyandmorty.presentation.core.model

import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.SummaryLocation

data class CharacterDisplayModel(
    val id: Int,
    val name: String,
    val status: CharacterStatusDisplayModel,
    val species: String,
    val type: String,
    val gender: String,
    val origin: SummaryLocation,
    val originDetail: LocationDetail?,
    val location: SummaryLocation,
    val locationDetail: LocationDetail?,
    val image: String,
    val episodeIds: List<Int>,
    val episodeDetails: List<EpisodeDetail> = emptyList(),
    val isFavourite: Boolean = false,
)