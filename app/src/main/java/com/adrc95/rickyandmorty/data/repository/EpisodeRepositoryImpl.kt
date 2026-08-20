package com.adrc95.rickyandmorty.data.repository

import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.repository.EpisodeRepository
import org.koin.core.annotation.Single

@Single(binds = [EpisodeRepository::class])
class EpisodeRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : EpisodeRepository {

    override suspend fun getEpisodes(characterId: Int, episodeIds: List<Int>): Result<List<EpisodeDetail>> {
        val cached = localDataSource.getEpisodesByCharacterId(characterId)
        if (cached.isNotEmpty()) return Result.Success(cached)

        return when (val result = remoteDataSource.getEpisodesByIds(episodeIds)) {
            is Result.Success -> {
                localDataSource.saveEpisodeDetails(result.data, characterId)
                Result.Success(result.data)
            }

            is Result.Error -> result
        }
    }
}
