package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_details",
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
data class LocationDetailEntity(
    @PrimaryKey val id: Int,
    val characterId: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: Int,
    val isOrigin: Boolean,
)
