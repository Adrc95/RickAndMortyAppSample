package com.adrc95.rickyandmorty.framework.database.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterPagingSourceTest {

    @Test
    fun givenAnchorNearTop_whenGetRefreshKey_thenClipsToZero() {
        val state = pagingState(anchorPosition = 5, initialLoadSize = 60)

        val refreshKey = source().getRefreshKey(state)

        assertEquals(0, refreshKey)
    }

    @Test
    fun givenAnchorPastHalfInitialLoad_whenGetRefreshKey_thenCentersTheAnchor() {
        val state = pagingState(anchorPosition = 100, initialLoadSize = 60)

        val refreshKey = source().getRefreshKey(state)

        assertEquals(70, refreshKey)
    }

    @Test
    fun givenNoAnchor_whenGetRefreshKey_thenReturnsNull() {
        val state = pagingState(anchorPosition = null, initialLoadSize = 60)

        val refreshKey = source().getRefreshKey(state)

        assertEquals(null, refreshKey)
    }

    private fun source() = CharacterPagingSource(FakeEntityPagingSource())

    private fun pagingState(anchorPosition: Int?, initialLoadSize: Int): PagingState<Int, Character> =
        PagingState(
            pages = listOf(
                LoadResult.Page(
                    data = emptyList<Character>(),
                    prevKey = null,
                    nextKey = initialLoadSize
                )
            ),
            anchorPosition = anchorPosition,
            config = PagingConfig(pageSize = initialLoadSize / 3, initialLoadSize = initialLoadSize),
            leadingPlaceholderCount = 0
        )

    private class FakeEntityPagingSource : PagingSource<Int, CharacterEntity>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> =
            LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)

        override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? = null
    }
}
