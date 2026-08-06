package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity

fun CharacterEntity.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = SummaryLocation(
        originId ?: -1,
        originName
    ),
    location = SummaryLocation(
        locationId ?: -1,
        locationName
    ),
    image = image,
    episodeIds = episodeIds,
    created = created,
    isFavourite = isFavourite
)
