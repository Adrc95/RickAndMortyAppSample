package com.adrc95.rickyandmorty.testing

import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import kotlinx.coroutines.Dispatchers

class TestPagingDataDiffer<T : Any> : PagingDataPresenter<T>(
    mainContext = Dispatchers.Unconfined
) {
    override suspend fun presentPagingDataEvent(event: PagingDataEvent<T>) = Unit
}
