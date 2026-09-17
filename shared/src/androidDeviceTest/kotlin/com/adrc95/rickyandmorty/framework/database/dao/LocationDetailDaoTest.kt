package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationDetailDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: LocationDetailDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.locationDetailDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenMissingLocation_whenGetByCharacterId_thenReturnsNull() = runTest {
        assertNull(dao.getByCharacterId(1, isOrigin = true))
    }

    @Test
    fun givenLocations_whenGetByCharacterId_thenReturnsMatchingOriginAndLocation() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        val origin = locationDetailEntity {
            withId(1)
            withIsOrigin(true)
        }
        val location = locationDetailEntity {
            withId(2)
            withIsOrigin(false)
        }
        dao.insert(origin)
        dao.insert(location)

        assertEquals(origin, dao.getByCharacterId(1, isOrigin = true))
        assertEquals(location, dao.getByCharacterId(1, isOrigin = false))
    }

    @Test
    fun givenExistingLocation_whenInsertWithSameId_thenReplacesIt() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        dao.insert(locationDetailEntity())
        val replacement = locationDetailEntity { withIsOrigin(false) }

        dao.insert(replacement)

        assertEquals(replacement, dao.getByCharacterId(1, isOrigin = false))
        assertNull(dao.getByCharacterId(1, isOrigin = true))
    }

    @Test
    fun givenCharacterLocations_whenDeleteByCharacterId_thenRemovesBothLocations() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        dao.insert(
            locationDetailEntity {
                withId(1)
                withIsOrigin(true)
            }
        )
        dao.insert(
            locationDetailEntity {
                withId(2)
                withIsOrigin(false)
            }
        )

        dao.deleteByCharacterId(1)

        assertNull(dao.getByCharacterId(1, isOrigin = true))
        assertNull(dao.getByCharacterId(1, isOrigin = false))
    }
}
