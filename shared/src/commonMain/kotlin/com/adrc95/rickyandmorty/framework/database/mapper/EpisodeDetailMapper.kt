package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.framework.database.entity.EpisodeDetailEntity
fun EpisodeDetail.toEntity(characterId: Int): EpisodeDetailEntity = EpisodeDetailEntity(
    id = id,
    characterId = characterId,
    name = name,
    episode = episode,
    airDate = airDate
)
