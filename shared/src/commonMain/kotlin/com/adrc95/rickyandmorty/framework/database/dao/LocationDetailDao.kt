package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adrc95.rickyandmorty.framework.database.entity.LocationDetailEntity

@Dao
interface LocationDetailDao {
    @Query("SELECT * FROM location_details WHERE characterId = :characterId AND isOrigin = :isOrigin LIMIT 1")
    suspend fun getByCharacterId(characterId: Int, isOrigin: Boolean): LocationDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationDetailEntity)

    @Query("DELETE FROM location_details WHERE characterId = :characterId")
    suspend fun deleteByCharacterId(characterId: Int)
}
