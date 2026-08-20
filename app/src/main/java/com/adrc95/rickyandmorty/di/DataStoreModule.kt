package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.adrc95.rickyandmorty.data.DataConstants.DATA_STORE_FILE
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataStoreModule {

    @Single
    fun provideDataStore(context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(DATA_STORE_FILE)
    }
}
