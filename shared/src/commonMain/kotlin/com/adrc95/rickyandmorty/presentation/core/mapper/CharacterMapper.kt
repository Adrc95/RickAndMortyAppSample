package com.adrc95.rickyandmorty.presentation.core.mapper

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel

fun Character.toDisplayModel(
    originDetail: LocationDetail? = null,
    locationDetail: LocationDetail? = null,
    episodeDetails: List<EpisodeDetail> = emptyList()
): CharacterDisplayModel = CharacterDisplayModel(
    id = id,
    name = name,
    status = CharacterStatusDisplayModel.from(status),
    species = species,
    type = type,
    gender = gender,
    origin = origin,
    originDetail = originDetail,
    location = location,
    locationDetail = locationDetail,
    image = image,
    episodeIds = episodeIds,
    episodeDetails = episodeDetails,
    isFavourite = isFavourite
)
