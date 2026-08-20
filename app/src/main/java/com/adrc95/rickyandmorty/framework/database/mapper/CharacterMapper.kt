package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    originName = origin.name,
    originId = origin.id,
    locationName = location.name,
    locationId = location.id,
    image = image,
    episodeIds = episodeIds,
    created = created
)
