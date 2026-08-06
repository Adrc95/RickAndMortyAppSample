package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.CHARACTER_TABLE

@Entity(
    tableName = CHARACTER_TABLE
)
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val originId: Int?,
    val locationName: String,
    val locationId: Int?,
    val image: String,
    val episodeIds: List<Int>,
    val created: String,
    val isFavourite: Boolean = false
)
