package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.room.Room
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.DATABASE_NAME
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataBaseModule {
    @Single
    fun providesDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = DATABASE_NAME
    ).build()

    @Single
    fun providesCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()

    @Single
    fun providesRemoteKeyDao(db: AppDatabase): RemoteKeyDao = db.remoteKeyDao()

    @Single
    fun providesLocationDetailDao(db: AppDatabase): LocationDetailDao = db.locationDetailDao()

    @Single
    fun providesEpisodeDetailDao(db: AppDatabase): EpisodeDetailDao = db.episodeDetailDao()
}
