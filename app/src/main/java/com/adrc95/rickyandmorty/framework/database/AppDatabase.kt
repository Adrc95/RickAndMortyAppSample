package com.adrc95.rickyandmorty.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import com.adrc95.rickyandmorty.framework.database.entity.EpisodeDetailEntity
import com.adrc95.rickyandmorty.framework.database.entity.LocationDetailEntity
import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity

@Database(
    entities = [
        CharacterEntity::class,
        RemoteKeyEntity::class,
        LocationDetailEntity::class,
        EpisodeDetailEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun locationDetailDao(): LocationDetailDao
    abstract fun episodeDetailDao(): EpisodeDetailDao
}
