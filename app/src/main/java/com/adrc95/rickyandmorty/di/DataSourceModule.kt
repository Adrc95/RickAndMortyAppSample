package com.adrc95.rickyandmorty.di

import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteRetrofitDataSource
import com.adrc95.rickyandmorty.data.datasource.RoomDataSource
import com.adrc95.rickyandmorty.data.datasource.SettingsPreferenceDataSource
import com.adrc95.rickyandmorty.data.datasource.SettingsPreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteRetrofitDataSource: RemoteRetrofitDataSource): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(roomDataSource: RoomDataSource): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindSettingsPreferenceDataSource(
        settingsPreferencesDataSource: SettingsPreferencesDataSource
    ): SettingsPreferenceDataSource
}
