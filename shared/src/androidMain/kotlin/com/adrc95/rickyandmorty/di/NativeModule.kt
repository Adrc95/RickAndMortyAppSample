package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import coil3.ImageLoader
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.getDatabaseBuilder
import com.adrc95.rickyandmorty.framework.image.createImageLoader
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.cache.OkioCacheStorage
import com.adrc95.rickyandmorty.framework.preference.getDataStore
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.Path.Companion.toPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class NativeModule {

    @Single
    fun provideRoomDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> =
        getDatabaseBuilder(context)

    @Single
    fun provideDataStore(context: Context): DataStore<Preferences> = getDataStore(context)

    @Single
    fun provideCacheStorage(context: Context): CacheStorage =
        OkioCacheStorage(context.cacheDir.resolve(CACHE_DIR).absolutePath.toPath())

    @Single
    fun provideImageLoader(context: Context): ImageLoader =
        createImageLoader(context, context.cacheDir.resolve(IMAGE_CACHE_DIR).absolutePath.toPath())
}
