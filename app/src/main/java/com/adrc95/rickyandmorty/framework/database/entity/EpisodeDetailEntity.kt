package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "episode_details",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("characterId")]
)
data class EpisodeDetailEntity(
    @PrimaryKey val id: Int,
    val characterId: Int,
    val name: String,
    val episode: String,
    val airDate: String,
)
