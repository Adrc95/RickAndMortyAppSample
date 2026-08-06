package com.adrc95.rickyandmorty.framework.database.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: CharacterDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        dao = database.characterDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenEmptyDatabase_whenGetById_thenReturnsNull() = runTest {
        assertNull(dao.getById(999))
    }

    @Test
    fun givenCharacters_whenInsertAll_thenCanReadByIdAndCount() = runTest {
        val characters = listOf(
            characterEntity(),
            characterEntity { withId(2); withName("Morty Smith") },
        )

        dao.insertAll(characters)

        assertEquals(2, dao.count())
        assertEquals(characters[0], dao.getById(1))
    }

    @Test
    fun givenExistingCharacter_whenInsertAllWithSameId_thenReplacesIt() = runTest {
        dao.insertAll(listOf(characterEntity { withName("Old name") }))

        val replacement = characterEntity { withName("New name") }
        dao.insertAll(listOf(replacement))

        assertEquals(replacement, dao.getById(1))
        assertEquals(1, dao.count())
    }

    @Test
    fun givenCharacters_whenClear_thenRemovesAllCharacters() = runTest {
        dao.insertAll(listOf(characterEntity()))

        dao.clear()

        assertEquals(0, dao.count())
        assertNull(dao.getById(1))
    }

    @Test
    fun givenCharacter_whenToggleFavourite_thenEmitsUpdatedValue() = runTest {
        dao.insertAll(listOf(characterEntity()))

        dao.isFavourite(1).test {
            assertEquals(false, awaitItem())
            dao.toggleFavourite(1)
            assertEquals(true, awaitItem())
            dao.toggleFavourite(1)
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenCharacters_whenLoadPagingSource_thenReturnsCharactersOrderedById() = runTest {
        val characters = listOf(
            characterEntity { withId(2); withName("Morty Smith") },
            characterEntity { withId(1) },
        )
        dao.insertAll(characters)

        val result = dao.getCharacters().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            )
        ) as PagingSource.LoadResult.Page

        assertEquals(listOf(characters[1], characters[0]), result.data)
    }
}
