package com.adrc95.rickyandmorty.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.REMOTE_KEY_TABLE

@Entity(
    tableName = REMOTE_KEY_TABLE
)
data class RemoteKeyEntity(
    @PrimaryKey
    val resource: String,
    val nextPage: Int?,
    val lastUpdatedAt: Long = 0L,
)