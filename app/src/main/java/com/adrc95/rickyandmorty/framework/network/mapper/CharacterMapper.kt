package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.framework.network.dto.CharacterDto
import com.adrc95.rickyandmorty.framework.network.dto.SummaryLocationDto

fun CharacterDto.toDomain(): Character = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin.toDomain(),
    location = location.toDomain(),
    image = image,
    episodeIds = episode.map { it.substringAfterLast("/").toIntOrNull() ?: -1 },
    created = created
)

fun SummaryLocationDto.toDomain(): SummaryLocation = SummaryLocation(
    id = url.substringAfterLast("/").toIntOrNull() ?: -1,
    name = name
)
