package com.adrc95.rickyandmorty.di

import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.data.datasource.SettingsPreferenceDataSource
import com.adrc95.rickyandmorty.data.paging.CharacterRemoteMediator
import com.adrc95.rickyandmorty.data.repository.CharacterRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.EpisodeRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.LocationRepositoryImpl
import com.adrc95.rickyandmorty.data.repository.SettingsRepositoryImpl
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import com.adrc95.rickyandmorty.domain.repository.EpisodeRepository
import com.adrc95.rickyandmorty.domain.repository.LocationRepository
import com.adrc95.rickyandmorty.domain.repository.SettingsRepository
import com.adrc95.rickyandmorty.framework.database.datasource.RoomDataSource
import com.adrc95.rickyandmorty.framework.network.datasource.RemoteKtorfitDataSource
import com.adrc95.rickyandmorty.framework.preference.datasource.DataStorePreferencesDataSource
import com.adrc95.rickyandmorty.framework.preference.datasource.SettingsPreferencesDataSource
import org.koin.dsl.module

val dataTestModule = module {
    single<LocalDataSource> {
        RoomDataSource(
            database = get(),
            characterDao = get(),
            remoteKeyDao = get(),
            locationDetailDao = get(),
            episodeDetailDao = get()
        )
    }
    single<RemoteDataSource> {
        RemoteKtorfitDataSource(
            characterService = get(),
            locationService = get(),
            episodeService = get()
        )
    }
    single { CharacterRemoteMediator(get(), get()) }
    single<DataStorePreferencesDataSource> { DataStorePreferencesDataSource(get()) }
    single<SettingsPreferenceDataSource> { SettingsPreferencesDataSource(get()) }
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get(), get()) }
    single<EpisodeRepository> { EpisodeRepositoryImpl(get(), get()) }
    single<LocationRepository> { LocationRepositoryImpl(get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}
