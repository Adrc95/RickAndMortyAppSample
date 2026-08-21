package com.adrc95.rickyandmorty.data.repository

import androidx.paging.LoadState
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.adrc95.rickyandmorty.di.dataTestModule
import com.adrc95.rickyandmorty.di.databaseTestModule
import com.adrc95.rickyandmorty.di.networkTestModule
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.AppErrorException
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import com.adrc95.rickyandmorty.framework.database.builder.remoteKeyEntity
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerRule
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerUrlHolder
import com.adrc95.rickyandmorty.testing.KoinTestRule
import com.adrc95.rickyandmorty.testing.createPagingDataDiffer
import com.adrc95.rickyandmorty.testing.extension.readJsonAsset
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CharacterRepositoryIntegrationTest : KoinTest {

    @get:Rule(order = 0)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 1)
    val koinRule = KoinTestRule(
        modules = listOf(
            dataTestModule,
            networkTestModule,
            databaseTestModule
        )
    )

    private val repository: CharacterRepository by inject()

    private val database: AppDatabase by inject()

    @After
    fun tearDown() {
        database.close()
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun givenRemoteCharacter_whenGetCharacterDetail_thenReturnsCharacterAndUsesExpectedPath() = runTest {
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("character_detail.json".readJsonAsset())
        )

        repository.getCharacterDetail(1).test {
            val character = awaitItem()

            assertEquals(1, character.id)
            assertEquals("Rick Sanchez", character.name)
            assertEquals("Alive", character.status)
            assertEquals("/character/1", mockWebServerRule.server.takeRequest().path)
            awaitComplete()
        }
    }

    @Test
    fun givenCachedCharacter_whenGetCharacterDetail_thenEmitsCacheAndThenRemoteCharacter() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("character_detail.json".readJsonAsset())
        )

        repository.getCharacterDetail(1).test {
            assertEquals("Rick Sanchez", awaitItem().name)
            assertEquals("Rick Sanchez", awaitItem().name)
            awaitComplete()
        }

        assertEquals(1, mockWebServerRule.server.requestCount)
    }

    @Test
    fun givenRemoteFailureAndNoCache_whenGetCharacterDetail_thenReturnsServerError() = runTest {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))

        repository.getCharacterDetail(1).test {
            val error = awaitError()

            assertTrue(error is AppErrorException)
            assertTrue((error as AppErrorException).error is AppError.Server)
        }

        assertEquals("/character/1", mockWebServerRule.server.takeRequest().path)
    }

    @Test
    fun givenCachedCharacter_whenRemoteFails_thenKeepsCachedCharacter() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))

        repository.getCharacterDetail(1).test {
            assertEquals("Rick Sanchez", awaitItem().name)
            awaitComplete()
        }

        assertEquals(1, mockWebServerRule.server.requestCount)
    }

    @Test
    fun givenRemoteCharacters_whenGetCharacters_thenPersistsAndEmitsCharacters() = runTest {
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("characters_page_1.json".readJsonAsset())
        )
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        val collectJob = launch {
            repository.getCharacters().collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        differ.onPagesUpdatedFlow.first {
            mockWebServerRule.server.requestCount == 1 && differ.itemCount > 0
        }

        assertEquals("/character?page=1", mockWebServerRule.server.takeRequest().path)
        assertEquals(1, database.characterDao().count())
        assertEquals("Rick Sanchez", differ.snapshot()[0]!!.name)

        collectJob.cancelAndJoin()
    }

    @Test
    fun givenSearchFilters_whenSearchCharacters_thenSendsFiltersAndPersistsResult() = runTest {
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("search_characters.json".readJsonAsset())
        )
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.searchCharacters("rick", "Human", "Male", "Alive").test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.onPagesUpdatedFlow.first()
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(
            "/character?page=1&name=rick&species=Human&gender=Male&status=Alive",
            mockWebServerRule.server.takeRequest().path
        )
        assertEquals(1, database.characterDao().count())
        assertEquals("Rick Sanchez", differ.snapshot()[0]!!.name)
    }

    @Test
    fun givenSearchReturnsNotFound_whenSearchCharacters_thenEmitsEmptyPage() = runTest {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(404).setBody("{}"))
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.searchCharacters("unknown", null, null, null).test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.onPagesUpdatedFlow.first()
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(differ.snapshot().isEmpty())
        assertEquals("/character?page=1&name=unknown", mockWebServerRule.server.takeRequest().path)
    }

    @Test
    fun givenSearchServerError_whenSearchCharacters_thenExposesPagingError() = runTest {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.searchCharacters("rick", null, null, null).test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.loadStateFlow.first { it.refresh is LoadState.Error }
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(differ.loadStateFlow.first { it.refresh is LoadState.Error }.refresh is LoadState.Error)
        assertEquals("/character?page=1&name=rick", mockWebServerRule.server.takeRequest().path)
    }

    @Test
    fun givenFreshCharactersCache_whenGetCharacters_thenDoesNotCallRemote() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        database.remoteKeyDao().insert(
            remoteKeyEntity {
                withNextPage(null)
                withLastUpdatedAt(System.currentTimeMillis())
            }
        )
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.getCharacters().test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.onPagesUpdatedFlow.first()
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(0, mockWebServerRule.server.requestCount)
        assertEquals("Rick Sanchez", differ.snapshot()[0]!!.name)
    }

    @Test
    fun givenRemoteFailureAndNoCache_whenGetCharacters_thenExposesRefreshError() = runTest {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.getCharacters().test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.loadStateFlow.first { it.refresh is LoadState.Error }
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertTrue(differ.loadStateFlow.first { it.refresh is LoadState.Error }.refresh is LoadState.Error)
        assertEquals("/character?page=1", mockWebServerRule.server.takeRequest().path)
    }

    @Test
    fun givenLastRemotePage_whenGetCharacters_thenDoesNotRequestThirdPage() = runTest {
        mockWebServerRule.server.enqueue(
            MockResponse().setResponseCode(200).setBody("characters_page_1.json".readJsonAsset())
        )
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        val collectJob = launch {
            repository.getCharacters().collectLatest { pagingData ->
                differ.submitData(pagingData)
            }
        }

        differ.onPagesUpdatedFlow.first {
            mockWebServerRule.server.requestCount == 1 && differ.itemCount > 0
        }
        differ.getItem(0)

        assertEquals(1, mockWebServerRule.server.requestCount)
        assertEquals(1, differ.snapshot().size)

        collectJob.cancelAndJoin()
    }

    @Test
    fun givenSearchQueryWithSpecialCharacters_whenSearchCharacters_thenEncodesRequestQuery() = runTest {
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(404).setBody("{}"))
        val differ = createPagingDataDiffer<Character>(
            areItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )

        repository.searchCharacters("rick & morty", null, null, null).test {
            val submitJob = launch { differ.submitData(awaitItem()) }
            differ.onPagesUpdatedFlow.first()
            submitJob.cancel()
            cancelAndIgnoreRemainingEvents()
        }

        assertEquals(
            "/character?page=1&name=rick%20%26%20morty",
            mockWebServerRule.server.takeRequest().path
        )
    }

    @Test
    fun givenNetworkDisconnect_whenGetCharacterDetail_thenReturnsConnectivityError() = runTest {
        mockWebServerRule.server.enqueue(
            MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)
        )

        repository.getCharacterDetail(1).test {
            val error = awaitError()

            assertTrue(error is AppErrorException)
            assertEquals(AppError.Connectivity, (error as AppErrorException).error)
        }
    }

    @Test
    fun givenStoredCharacter_whenToggleFavourite_thenUpdatesRoomBackedFlow() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))

        repository.isFavourite(1).test {
            assertEquals(false, awaitItem())
            repository.toggleFavourite(1)
            assertEquals(true, awaitItem())
            repository.toggleFavourite(1)
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
