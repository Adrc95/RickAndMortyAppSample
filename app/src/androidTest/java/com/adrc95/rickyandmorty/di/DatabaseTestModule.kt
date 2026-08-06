package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.room.Room
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataBaseModule::class],
)
object DatabaseTestModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideCharacterDao(database: AppDatabase): CharacterDao = database.characterDao()

    @Provides
    fun provideRemoteKeyDao(database: AppDatabase): RemoteKeyDao = database.remoteKeyDao()

    @Provides
    fun provideLocationDetailDao(database: AppDatabase): LocationDetailDao = database.locationDetailDao()

    @Provides
    fun provideEpisodeDetailDao(database: AppDatabase): EpisodeDetailDao = database.episodeDetailDao()
}
