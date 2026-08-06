package com.adrc95.rickyandmorty.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adrc95.rickyandmorty.framework.database.entity.EpisodeDetailEntity

@Dao
interface EpisodeDetailDao {
    @Query("SELECT * FROM episode_details WHERE characterId = :characterId")
    suspend fun getByCharacterId(characterId: Int): List<EpisodeDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeDetailEntity>)

    @Query("DELETE FROM episode_details WHERE characterId = :characterId")
    suspend fun deleteByCharacterId(characterId: Int)
}
