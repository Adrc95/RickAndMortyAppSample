package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "remote_keys"
)
data class RemoteKeyEntity(
    @PrimaryKey
    val resource: String,
    val nextPage: Int?,
    val lastUpdatedAt: Long = 0L,
)