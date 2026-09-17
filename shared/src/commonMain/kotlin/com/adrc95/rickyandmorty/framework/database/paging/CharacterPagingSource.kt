package com.adrc95.rickyandmorty.framework.database.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import com.adrc95.rickyandmorty.framework.database.mapper.toDomain

class CharacterPagingSource(private val source: PagingSource<Int, CharacterEntity>) : PagingSource<Int, Character>() {

    init {
        source.registerInvalidatedCallback { invalidate() }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> =
        when (val result = source.load(params)) {
            is LoadResult.Error -> LoadResult.Error(result.throwable)

            is LoadResult.Invalid -> LoadResult.Invalid()

            is LoadResult.Page -> LoadResult.Page(
                data = result.data.map { it.toDomain() },
                prevKey = result.prevKey,
                nextKey = result.nextKey,
                itemsBefore = result.itemsBefore,
                itemsAfter = result.itemsAfter
            )
        }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = state.anchorPosition?.let { anchor ->
        state.closestPageToPosition(anchor)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
    }
}
