package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject

class GetEpisodesByIdsUseCase @Inject constructor(
    private val episodeRepository: EpisodeRepository,
) {
    suspend operator fun invoke(characterId: Int, episodeIds: List<Int>): Result<List<EpisodeDetail>> =
        episodeRepository.getEpisodes(characterId, episodeIds)
}
