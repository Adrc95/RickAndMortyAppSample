package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.CHARACTER_ID_COLUMN
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.EPISODE_DETAIL_TABLE
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.ID_COLUMN

@Entity(
    tableName = EPISODE_DETAIL_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = [ID_COLUMN],
            childColumns = [CHARACTER_ID_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(CHARACTER_ID_COLUMN)]
)
data class EpisodeDetailEntity(
    @PrimaryKey val id: Int,
    val characterId: Int,
    val name: String,
    val episode: String,
    val airDate: String,
)
