package com.adrc95.rickyandmorty.domain.repository

import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail

interface EpisodeRepository {
    suspend fun getEpisodes(characterId: Int, episodeIds: List<Int>): Result<List<EpisodeDetail>>
}
