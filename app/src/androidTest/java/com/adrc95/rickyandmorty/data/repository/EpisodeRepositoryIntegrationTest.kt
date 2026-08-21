package com.adrc95.rickyandmorty.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.di.dataTestModule
import com.adrc95.rickyandmorty.di.databaseTestModule
import com.adrc95.rickyandmorty.di.networkTestModule
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.repository.EpisodeRepository
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import com.adrc95.rickyandmorty.framework.database.mapper.toDomain
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerRule
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerUrlHolder
import com.adrc95.rickyandmorty.testing.KoinTestRule
import com.adrc95.rickyandmorty.testing.extension.readJsonAsset
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class EpisodeRepositoryIntegrationTest : KoinTest {

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

    private val repository: EpisodeRepository by inject()

    private val database: AppDatabase by inject()

    @After
    fun tearDown() {
        database.close()
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun givenSingleEpisodeNotCached_whenGetEpisodes_thenFetchesAndPersistsEpisode() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("episode_detail.json".readJsonAsset())
        )

        val result = repository.getEpisodes(characterId = 1, episodeIds = listOf(1))

        assertEquals(
            Result.Success(
                listOf(
                    EpisodeDetail(
                        id = 1,
                        name = "Pilot",
                        episode = "S01E01",
                        airDate = "December 2, 2013"
                    )
                )
            ),
            result
        )
        assertEquals("/episode/1", mockWebServerRule.server.takeRequest().path)
        assertEquals(
            episodeDetailEntity {
                withId(1)
                withCharacterId(1)
            },
            database.episodeDetailDao().getByCharacterId(1).single()
        )
    }

    @Test
    fun givenMultipleEpisodesNotCached_whenGetEpisodes_thenFetchesAllEpisodesAndPersistsThem() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("episodes_detail.json".readJsonAsset())
        )

        val result = repository.getEpisodes(characterId = 1, episodeIds = listOf(1, 2))

        assertEquals(2, (result as Result.Success).data.size)
        assertEquals("/episode/1,2", mockWebServerRule.server.takeRequest().path)
        assertEquals(2, database.episodeDetailDao().getByCharacterId(1).size)
    }

    @Test
    fun givenEpisodesCached_whenGetEpisodes_thenReturnsCacheWithoutCallingRemote() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        val cached = episodeDetailEntity {
            withId(1)
            withCharacterId(1)
        }
        database.episodeDetailDao().insertAll(listOf(cached))

        val result = repository.getEpisodes(characterId = 1, episodeIds = listOf(999))

        assertEquals(Result.Success(listOf(cached.toDomain())), result)
        assertEquals(0, mockWebServerRule.server.requestCount)
    }

    @Test
    fun givenRemoteFailureAndNoCache_whenGetEpisodes_thenReturnsErrorAndDoesNotPersist() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))

        val result = repository.getEpisodes(characterId = 1, episodeIds = listOf(1))

        assertEquals(Result.Error(AppError.Server(500)), result)
        assertEquals("/episode/1", mockWebServerRule.server.takeRequest().path)
        assertTrue(database.episodeDetailDao().getByCharacterId(1).isEmpty())
    }

    @Test
    fun givenEpisodesCachedForAnotherCharacter_whenGetEpisodes_thenFetchesRequestedCharacterEpisodes() = runTest {
        database.characterDao().insertAll(
            listOf(
                characterEntity { withId(1) },
                characterEntity {
                    withId(2)
                    withName("Morty Smith")
                }
            )
        )
        database.episodeDetailDao().insertAll(
            listOf(
                episodeDetailEntity {
                    withId(1)
                    withCharacterId(2)
                }
            )
        )
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("episode_detail.json".readJsonAsset())
        )

        repository.getEpisodes(characterId = 1, episodeIds = listOf(1))

        assertEquals("/episode/1", mockWebServerRule.server.takeRequest().path)
        assertEquals(1, database.episodeDetailDao().getByCharacterId(1).single().characterId)
    }
}
