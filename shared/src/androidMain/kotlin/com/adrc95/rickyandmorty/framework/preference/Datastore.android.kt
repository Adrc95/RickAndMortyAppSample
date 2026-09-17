package com.adrc95.rickyandmorty.framework.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.adrc95.rickyandmorty.data.DataConstants.DATA_STORE_FILE

fun getDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = {
        context.filesDir.resolve(DATA_STORE_FILE).absolutePath
    }
)

