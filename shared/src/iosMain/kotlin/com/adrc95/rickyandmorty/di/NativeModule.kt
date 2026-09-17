package com.adrc95.rickyandmorty.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import coil3.ImageLoader
import coil3.PlatformContext
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.getDatabaseBuilder
import com.adrc95.rickyandmorty.framework.image.createImageLoader
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.cache.OkioCacheStorage
import com.adrc95.rickyandmorty.framework.preference.getDataStore
import io.ktor.client.plugins.cache.storage.CacheStorage
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUserDomainMask

@Module
@Configuration
class NativeModule {

    @Single
    fun provideRoomDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> = getDatabaseBuilder()

    @Single
    fun provideDataStore(): DataStore<Preferences> = getDataStore()

    @Single
    fun provideCacheStorage(): CacheStorage = OkioCacheStorage(cachesDirectory().resolve(CACHE_DIR))

    @Single
    fun provideImageLoader(): ImageLoader =
        createImageLoader(PlatformContext.INSTANCE, cachesDirectory().resolve(IMAGE_CACHE_DIR))

    @OptIn(ExperimentalForeignApi::class)
    private fun cachesDirectory(): Path {
        val url = NSFileManager.defaultManager.URLForDirectory(
            directory = NSCachesDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = true,
            error = null,
        )
        return (url?.path ?: NSTemporaryDirectory()).toPath()
    }
}
