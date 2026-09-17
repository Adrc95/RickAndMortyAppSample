package com.adrc95.rickyandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.adrc95.rickyandmorty.data.DataConstants.CACHE_TTL_MILLIS
import com.adrc95.rickyandmorty.data.DataConstants.CHARACTERS_RESOURCE
import com.adrc95.rickyandmorty.data.DataConstants.DEFAULT_LAST_UPDATED
import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.Character
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Single
import kotlin.time.Clock

@OptIn(ExperimentalPagingApi::class)
@Single
class CharacterRemoteMediator(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : RemoteMediator<Int, Character>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>,
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {
                val remoteKey = localDataSource.getRemoteKey(CHARACTERS_RESOURCE)
                val lastUpdatedAt = remoteKey?.lastUpdatedAt ?: DEFAULT_LAST_UPDATED
                val isFresh = localDataSource.hasCachedCharacters() &&
                    lastUpdatedAt > DEFAULT_LAST_UPDATED &&
                    Clock.System.now().toEpochMilliseconds() - lastUpdatedAt < CACHE_TTL_MILLIS
                if (isFresh) {
                    Napier.d("REFRESH: using CACHE (lastUpdated=$lastUpdatedAt)")
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                Napier.d("REFRESH: fetching from API")
                1
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                val remoteKey = localDataSource.getRemoteKey(CHARACTERS_RESOURCE)
                remoteKey?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return when (val result = remoteDataSource.getCharacters(page)) {

            is Result.Success -> {
                Napier.d("API success: page=$page, nextPage=${result.data.nextPage}")
                localDataSource.insertCharacters(
                    characters = result.data.data,
                    nextPage = result.data.nextPage,
                    deleteOld  = loadType == LoadType.REFRESH
                )
                MediatorResult.Success(
                    endOfPaginationReached =
                        result.data.nextPage == null
                )
            }

            is Result.Error -> {
                Napier.w("API error: page=$page, error=${result.error}")
                MediatorResult.Error(
                    Exception(result.error.toString())
                )
            }
        }
    }
}
