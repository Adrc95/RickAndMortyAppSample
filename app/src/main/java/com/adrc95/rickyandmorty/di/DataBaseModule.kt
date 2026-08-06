package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.room.Room
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.DATABASE_NAME
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providesCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()

    @Provides
    @Singleton
    fun providesRemoteKeyDao(db: AppDatabase): RemoteKeyDao = db.remoteKeyDao()

    @Provides
    @Singleton
    fun providesLocationDetailDao(db: AppDatabase): LocationDetailDao = db.locationDetailDao()

    @Provides
    @Singleton
    fun providesEpisodeDetailDao(db: AppDatabase): EpisodeDetailDao = db.episodeDetailDao()
}
