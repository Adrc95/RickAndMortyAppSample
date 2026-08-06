package com.adrc95.rickyandmorty.framework.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getCharacters(): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        characters: List<CharacterEntity>
    )

    @Query("DELETE FROM characters")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun count(): Int

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): CharacterEntity?

    @Query("UPDATE characters SET isFavourite = NOT isFavourite WHERE id = :characterId")
    suspend fun toggleFavourite(characterId: Int)

    @Query("SELECT isFavourite FROM characters WHERE id = :characterId")
    fun isFavourite(characterId: Int): Flow<Boolean>
}