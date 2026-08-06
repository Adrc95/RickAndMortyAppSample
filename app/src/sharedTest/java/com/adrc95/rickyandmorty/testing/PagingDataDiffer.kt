package com.adrc95.rickyandmorty.testing

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers

fun <T : Any> createPagingDataDiffer(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean,
) = AsyncPagingDataDiffer(
    diffCallback = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T) =
            areItemsTheSame.invoke(oldItem, newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T) =
            areContentsTheSame.invoke(oldItem, newItem)
    },
    updateCallback = NoOpListUpdateCallback,
    mainDispatcher = Dispatchers.Main,
    workerDispatcher = Dispatchers.Unconfined,
)

private object NoOpListUpdateCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
    override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
    override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
}
