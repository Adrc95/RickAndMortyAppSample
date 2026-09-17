package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.framework.database.entity.EpisodeDetailEntity

fun EpisodeDetailEntity.toDomain(): EpisodeDetail = EpisodeDetail(
    id = id,
    name = name,
    episode = episode,
    airDate = airDate
)
