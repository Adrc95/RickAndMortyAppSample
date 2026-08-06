package com.adrc95.rickyandmorty.di

import com.adrc95.rickyandmorty.data.repository.CharacterRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.EpisodeRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.LocationRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.SettingsRepositoryImpl
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import com.adrc95.rickyandmorty.domain.repository.EpisodeRepository
import com.adrc95.rickyandmorty.domain.repository.LocationRepository
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(characterRepositoryImpl: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindEpisodeRepository(episodeRepositoryImpl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
}
