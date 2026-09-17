package com.adrc95.rickyandmorty.di

import androidx.room.RoomDatabase
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataBaseModule {
    @Single
    fun providesAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase = builder.build()

    @Single
    fun providesCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()

    @Single
    fun providesRemoteKeyDao(db: AppDatabase): RemoteKeyDao = db.remoteKeyDao()

    @Single
    fun providesLocationDetailDao(db: AppDatabase): LocationDetailDao = db.locationDetailDao()

    @Single
    fun providesEpisodeDetailDao(db: AppDatabase): EpisodeDetailDao = db.episodeDetailDao()
}
