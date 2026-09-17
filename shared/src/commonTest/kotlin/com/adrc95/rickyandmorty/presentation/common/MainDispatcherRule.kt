package com.adrc95.rickyandmorty.presentation.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val scheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(scheduler),
) {
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    fun tearDown() {
        Dispatchers.resetMain()
    }
}
