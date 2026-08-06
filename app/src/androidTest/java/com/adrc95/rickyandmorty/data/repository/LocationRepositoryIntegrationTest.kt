package com.adrc95.rickyandmorty.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.repository.LocationRepository
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import com.adrc95.rickyandmorty.testing.extension.readJsonAsset
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerRule
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerUrlHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LocationRepositoryIntegrationTest {

    @get:Rule(order = 0)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: LocationRepository

    @Inject
    lateinit var database: AppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        database.close()
        MockWebServerUrlHolder.baseUrl = "http://localhost:8080/"
    }

    @Test
    fun givenLocationNotCached_whenGetLocation_thenFetchesRemoteAndPersistsIt() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("location_detail.json".readJsonAsset())
        )

        val result = repository.getLocation(characterId = 1, locationId = 3, isOrigin = false)

        assertEquals(
            Result.Success(
                LocationDetail(
                    id = 3,
                    name = "Citadel of Ricks",
                    type = "Space station",
                    dimension = "unknown",
                    residentsCount = 2,
                )
            ),
            result,
        )
        assertEquals("/location/3", mockWebServerRule.server.takeRequest().path)
        assertEquals(
            locationDetailEntity {
                withId(3)
                withCharacterId(1)
                withName("Citadel of Ricks")
                withType("Space station")
                withDimension("unknown")
                withResidents(2)
                withIsOrigin(false)
            },
            database.locationDetailDao().getByCharacterId(1, isOrigin = false),
        )
    }

    @Test
    fun givenLocationCached_whenGetLocation_thenReturnsCacheWithoutCallingRemote() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        val cached = locationDetailEntity { withId(3); withCharacterId(1); withIsOrigin(false) }
        database.locationDetailDao().insert(cached)

        val result = repository.getLocation(characterId = 1, locationId = 999, isOrigin = false)

        assertEquals(
            Result.Success(
                LocationDetail(
                    id = 3,
                    name = "Earth (C-137)",
                    type = "Planet",
                    dimension = "Dimension C-137",
                    residentsCount = 27,
                )
            ),
            result,
        )
        assertEquals(0, mockWebServerRule.server.requestCount)
    }

    @Test
    fun givenRemoteFailureAndNoCache_whenGetLocation_thenReturnsError() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        mockWebServerRule.server.enqueue(MockResponse().setResponseCode(500).setBody("{}"))

        val result = repository.getLocation(characterId = 1, locationId = 3, isOrigin = false)

        assertEquals(Result.Error(AppError.Server(500)), result)
        assertEquals("/location/3", mockWebServerRule.server.takeRequest().path)
        assertNull(database.locationDetailDao().getByCharacterId(1, isOrigin = false))
    }

    @Test
    fun givenOriginLocationCached_whenRequestingCurrentLocation_thenFetchesCurrentLocation() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        database.locationDetailDao().insert(
            locationDetailEntity { withId(1); withCharacterId(1); withIsOrigin(true) }
        )
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("location_detail.json".readJsonAsset())
        )

        val result = repository.getLocation(characterId = 1, locationId = 3, isOrigin = false)

        assertTrue(result is Result.Success)
        assertEquals("/location/3", mockWebServerRule.server.takeRequest().path)
        assertEquals(1, database.locationDetailDao().getByCharacterId(1, isOrigin = true)!!.id)
        assertEquals(3, database.locationDetailDao().getByCharacterId(1, isOrigin = false)!!.id)
    }

    @Test
    fun givenLocationCachedForAnotherCharacter_whenGetLocation_thenFetchesRequestedCharacterLocation() = runTest {
        database.characterDao().insertAll(
            listOf(
                characterEntity { withId(1) },
                characterEntity { withId(2); withName("Morty Smith") },
            )
        )
        database.locationDetailDao().insert(
            locationDetailEntity { withId(3); withCharacterId(2); withIsOrigin(false) }
        )
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("location_detail.json".readJsonAsset())
        )

        repository.getLocation(characterId = 1, locationId = 3, isOrigin = false)

        assertEquals("/location/3", mockWebServerRule.server.takeRequest().path)
        assertEquals(3, database.locationDetailDao().getByCharacterId(1, isOrigin = false)!!.id)
    }

}
