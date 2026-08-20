package com.adrc95.rickyandmorty.di

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.adrc95.rickyandmorty.BuildConfig
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CACHE_SIZE
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CONNECT_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_CACHE_DIR
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_CACHE_SIZE_BYTES
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.IMAGE_MEMORY_CACHE_PERCENT
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.JSON_TYPE
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.READ_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.WRITE_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.service.CharacterService
import com.adrc95.rickyandmorty.framework.network.service.EpisodeService
import com.adrc95.rickyandmorty.framework.network.service.LocationService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Path.Companion.toOkioPath
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import retrofit2.Retrofit

@Module
class NetworkModule {

    @Single
    @Named("baseUrl")
    fun provideBaseUrl(): String = BuildConfig.API_URL

    @Single
    fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Single
    fun provideCache(context: Context): Cache = Cache(
        directory = File(context.cacheDir, CACHE_DIR),
        maxSize = CACHE_SIZE
    )

    @Single
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Single
    fun provideImageLoader(context: Context, okHttpClient: OkHttpClient): ImageLoader = ImageLoader.Builder(context)
        .components {
            add(
                OkHttpNetworkFetcherFactory(
                    callFactory = { okHttpClient }
                )
            )
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, IMAGE_MEMORY_CACHE_PERCENT)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve(IMAGE_CACHE_DIR).toOkioPath())
                .maxSizeBytes(IMAGE_CACHE_SIZE_BYTES)
                .build()
        }
        .build()

    @Single
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = JSON_TYPE.toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Single
    fun provideCharacterService(retrofit: Retrofit): CharacterService = retrofit.create(CharacterService::class.java)

    @Single
    fun provideLocationService(retrofit: Retrofit): LocationService = retrofit.create(LocationService::class.java)

    @Single
    fun provideEpisodeService(retrofit: Retrofit): EpisodeService = retrofit.create(EpisodeService::class.java)
}
