package com.adrc95.rickyandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.adrc95.rickyandmorty.data.DataConstants.PAGING_ENABLED_PLACEHOLDER_DEFAULT
import com.adrc95.rickyandmorty.data.DataConstants.PAGING_INITIAL_DEFAULT
import com.adrc95.rickyandmorty.data.DataConstants.PAGING_PREFETCH_DEFAULT
import com.adrc95.rickyandmorty.data.DataConstants.PAGING_SIZE_DEFAULT
import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.data.paging.CharacterRemoteMediator
import com.adrc95.rickyandmorty.data.paging.SearchCharacterPagingSource
import com.adrc95.rickyandmorty.domain.exception.AppErrorException
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@OptIn(ExperimentalPagingApi::class)
@Single(binds = [CharacterRepository::class])
class CharacterRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val remoteMediator: CharacterRemoteMediator
) : CharacterRepository {

    override fun getCharacters(): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(
            initialLoadSize = PAGING_INITIAL_DEFAULT,
            pageSize = PAGING_SIZE_DEFAULT,
            prefetchDistance = PAGING_PREFETCH_DEFAULT,
            enablePlaceholders = PAGING_ENABLED_PLACEHOLDER_DEFAULT
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { localDataSource.getCharacters() }
    ).flow

    override fun searchCharacters(
        name: String?,
        species: String?,
        gender: String?,
        status: String?
    ): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(
            pageSize = PAGING_SIZE_DEFAULT,
            prefetchDistance = PAGING_PREFETCH_DEFAULT,
            enablePlaceholders = PAGING_ENABLED_PLACEHOLDER_DEFAULT
        ),
        pagingSourceFactory = {
            SearchCharacterPagingSource(remoteDataSource, localDataSource, name, species, gender, status)
        }
    ).flow

    override fun getCharacterDetail(id: Int): Flow<Character> = flow {
        val cached = localDataSource.getCharacterById(id)
        if (cached != null) {
            emit(cached)
        }
        when (val result = remoteDataSource.getCharacterById(id)) {
            is Result.Success -> {
                emit(result.data)
            }

            is Result.Error -> {
                if (cached == null) throw AppErrorException(result.error)
            }
        }
    }

    override fun isFavourite(characterId: Int): Flow<Boolean> = localDataSource.isFavourite(characterId)

    override suspend fun toggleFavourite(characterId: Int) {
        localDataSource.toggleFavourite(characterId)
    }
}
