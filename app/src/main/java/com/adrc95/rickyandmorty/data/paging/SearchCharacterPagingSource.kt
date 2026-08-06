package com.adrc95.rickyandmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adrc95.rickyandmorty.data.DataConstants.DEFAULT_PAGE
import com.adrc95.rickyandmorty.data.DataConstants.ERROR_404
import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.Character

class SearchCharacterPagingSource(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val name: String?,
    private val species: String?,
    private val gender: String?,
    private val status: String?
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: DEFAULT_PAGE
        return when (val result = remoteDataSource.searchCharacters(page, name, species, gender, status)) {
            is Result.Success -> {
                localDataSource.saveCharacters(result.data.data)
                LoadResult.Page(
                    data = result.data.data,
                    prevKey = if (page == DEFAULT_PAGE) null else page - 1,
                    nextKey = result.data.nextPage
                )
            }

            is Result.Error -> {
                if (result.error is AppError.Server && result.error.code == ERROR_404) {
                    LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
                } else {
                    LoadResult.Error(Exception(result.error.toString()))
                }
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = state.anchorPosition?.let { anchor ->
        state.closestPageToPosition(anchor)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
    }
}
