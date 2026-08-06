package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.builder.remoteKeyEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteKeyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: RemoteKeyDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.remoteKeyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenMissingResource_whenGet_thenReturnsNull() = runTest {
        assertNull(dao.get("characters"))
    }

    @Test
    fun givenRemoteKey_whenInsert_thenCanReadByResource() = runTest {
        val expected = remoteKeyEntity()

        dao.insert(expected)

        assertEquals(expected, dao.get("characters"))
    }

    @Test
    fun givenExistingResource_whenInsertWithSameResource_thenReplacesIt() = runTest {
        dao.insert(remoteKeyEntity { withNextPage(2) })
        val replacement = remoteKeyEntity { withNextPage(3) }

        dao.insert(replacement)

        assertEquals(replacement, dao.get("characters"))
    }

    @Test
    fun givenRemoteKeys_whenClear_thenRemovesAllResources() = runTest {
        dao.insert(remoteKeyEntity { withResource("characters") })
        dao.insert(remoteKeyEntity { withResource("episodes") })

        dao.clear()

        assertNull(dao.get("characters"))
        assertNull(dao.get("episodes"))
    }
}
