package com.adrc95.rickyandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.adrc95.rickyandmorty.data.DataConstants.CACHE_TTL_MILLIS
import com.adrc95.rickyandmorty.data.DataConstants.CHARACTERS_RESOURCE
import com.adrc95.rickyandmorty.data.DataConstants.DEFAULT_LAST_UPDATED
import com.adrc95.rickyandmorty.data.DataConstants.DEFAULT_PAGE
import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import javax.inject.Inject
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CharacterEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = localDataSource.getRemoteKey(CHARACTERS_RESOURCE)
                val lastUpdatedAt = remoteKey?.lastUpdatedAt ?: DEFAULT_LAST_UPDATED
                val isFresh = localDataSource.hasCachedCharacters() &&
                    lastUpdatedAt > DEFAULT_LAST_UPDATED &&
                    System.currentTimeMillis() - lastUpdatedAt < CACHE_TTL_MILLIS
                if (isFresh) {
                    Timber.d("REFRESH: using CACHE (lastUpdated=%d)", lastUpdatedAt)
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                Timber.d("REFRESH: fetching from API")
                DEFAULT_PAGE
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
                Timber.d(
                    "API success: page=%d, nextPage=%s",
                    page,
                    result.data.nextPage
                )
                localDataSource.insertCharacters(
                    characters = result.data.data,
                    nextPage = result.data.nextPage,
                    deleteOld = loadType == LoadType.REFRESH
                )
                MediatorResult.Success(
                    endOfPaginationReached =
                        result.data.nextPage == null
                )
            }

            is Result.Error -> {
                Timber.w("API error: page=%d, error=%s", page, result.error)
                MediatorResult.Error(
                    Exception(result.error.toString())
                )
            }
        }
    }
}
