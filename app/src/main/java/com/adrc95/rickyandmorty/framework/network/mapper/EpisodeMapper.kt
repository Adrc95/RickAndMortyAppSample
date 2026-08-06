package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.framework.network.dto.EpisodeDto

fun EpisodeDto.toDomain(): EpisodeDetail = EpisodeDetail(
    id = id,
    name = name,
    episode = episode,
    airDate = airDate
)
