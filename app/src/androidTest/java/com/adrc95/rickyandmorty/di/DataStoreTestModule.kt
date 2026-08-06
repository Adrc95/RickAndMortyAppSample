package com.adrc95.rickyandmorty.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import java.io.File
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataStoreModule::class]
)
object DataStoreTestModule {

    @Provides
    @Singleton
    fun provideDataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        File.createTempFile(
            "test_settings",
            ".preferences_pb"
        )
    }
}
