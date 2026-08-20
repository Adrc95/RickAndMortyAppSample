package com.adrc95.rickyandmorty.framework.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.adrc95.rickyandmorty.framework.getOrDefault
import com.adrc95.rickyandmorty.framework.remove
import com.adrc95.rickyandmorty.framework.set
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class DataStorePreferencesDataSource @Inject constructor(val dataStore: DataStore<Preferences>) {
    inline fun <reified T> getFlow(key: String, default: T): Flow<T> =
        dataStore.data.map { prefs -> prefs.getOrDefault<T>(key, default) }

    suspend inline fun <reified T> getOnce(key: String, default: T): T =
        dataStore.data.map { prefs -> prefs.getOrDefault<T>(key, default) }.first()

    suspend inline fun <reified T> set(key: String, value: T) {
        dataStore.edit { prefs -> prefs.set(key, value) }
    }

    suspend inline fun <reified T> remove(key: String) {
        dataStore.edit { prefs -> prefs.remove<T>(key) }
    }
}
