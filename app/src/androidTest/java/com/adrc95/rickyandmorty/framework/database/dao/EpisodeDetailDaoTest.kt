package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.characterEntity
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EpisodeDetailDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: EpisodeDetailDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).build()
        dao = database.episodeDetailDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenMissingEpisodes_whenGetByCharacterId_thenReturnsEmptyList() = runTest {
        assertEquals(emptyList<Any>(), dao.getByCharacterId(1))
    }

    @Test
    fun givenEpisodes_whenGetByCharacterId_thenReturnsOnlyMatchingCharacterEpisodes() = runTest {
        database.characterDao().insertAll(
            listOf(
                characterEntity { withId(1) },
                characterEntity { withId(2); withName("Morty Smith") },
            )
        )
        val rickEpisode = episodeDetailEntity { withId(1); withCharacterId(1) }
        val mortyEpisode = episodeDetailEntity { withId(2); withCharacterId(2) }
        dao.insertAll(listOf(rickEpisode, mortyEpisode))

        assertEquals(listOf(rickEpisode), dao.getByCharacterId(1))
    }

    @Test
    fun givenExistingEpisode_whenInsertWithSameId_thenReplacesIt() = runTest {
        database.characterDao().insertAll(listOf(characterEntity()))
        dao.insertAll(listOf(episodeDetailEntity()))
        val replacement = episodeDetailEntity { withCharacterId(1) }

        dao.insertAll(listOf(replacement))

        assertEquals(listOf(replacement), dao.getByCharacterId(1))
    }

    @Test
    fun givenCharacterEpisodes_whenDeleteByCharacterId_thenRemovesOnlyMatchingEpisodes() = runTest {
        database.characterDao().insertAll(
            listOf(
                characterEntity { withId(1) },
                characterEntity { withId(2); withName("Morty Smith") },
            )
        )
        val rickEpisode = episodeDetailEntity { withId(1); withCharacterId(1) }
        val mortyEpisode = episodeDetailEntity { withId(2); withCharacterId(2) }
        dao.insertAll(listOf(rickEpisode, mortyEpisode))

        dao.deleteByCharacterId(1)

        assertEquals(emptyList<Any>(), dao.getByCharacterId(1))
        assertEquals(listOf(mortyEpisode), dao.getByCharacterId(2))
    }
}
