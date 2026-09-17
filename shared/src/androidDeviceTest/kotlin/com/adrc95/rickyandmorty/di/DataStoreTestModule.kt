package com.adrc95.rickyandmorty.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File
import org.koin.dsl.module

val dataStoreTestModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            File.createTempFile("test_settings", ".preferences_pb")
        }
    }
}
