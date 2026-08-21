package com.adrc95.rickyandmorty.di

import android.content.Context
import coil3.ImageLoader
import com.adrc95.rickyandmorty.framework.network.service.CharacterService
import com.adrc95.rickyandmorty.framework.network.service.EpisodeService
import com.adrc95.rickyandmorty.framework.network.service.LocationService
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerUrlHolder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit

val networkTestModule = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    single { OkHttpClient.Builder().build() }
    single<ImageLoader> { ImageLoader.Builder(get<Context>()).build() }
    single {
        Retrofit.Builder()
            .baseUrl(MockWebServerUrlHolder.baseUrl)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
    single<CharacterService> { get<Retrofit>().create(CharacterService::class.java) }
    single<EpisodeService> { get<Retrofit>().create(EpisodeService::class.java) }
    single<LocationService> { get<Retrofit>().create(LocationService::class.java) }
}
