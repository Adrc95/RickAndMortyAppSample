package com.adrc95.rickyandmorty.domain.repository

import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail

class FakeEpisodeRepository : EpisodeRepository {

    private val episodesByCharacter = mutableMapOf<Int, List<EpisodeDetail>>()
    private var error: AppError? = null

    override suspend fun getEpisodes(characterId: Int, episodeIds: List<Int>): Result<List<EpisodeDetail>> {
        error?.let { return Result.Error(it) }
        val cached = episodesByCharacter[characterId]
        return if (cached != null) {
            Result.Success(cached)
        } else {
            Result.Success(emptyList())
        }
    }

    fun setEpisodes(characterId: Int, episodes: List<EpisodeDetail>) {
        episodesByCharacter[characterId] = episodes
    }

    fun setError(error: AppError?) {
        this.error = error
    }
}
