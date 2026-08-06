package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.REMOTE_KEY_TABLE
import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Query("DELETE FROM $REMOTE_KEY_TABLE")
    suspend fun clear()

    @Query("SELECT * FROM $REMOTE_KEY_TABLE WHERE resource = :resource")
    suspend fun get(resource: String): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKeyEntity)
}