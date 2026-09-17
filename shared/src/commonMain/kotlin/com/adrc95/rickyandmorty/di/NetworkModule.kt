package com.adrc95.rickyandmorty.di

import com.adrc95.rickyandmorty.shared.BuildConfig
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.CONNECT_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.READ_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.NetworkConstants.WRITE_TIMEOUT_SECONDS
import com.adrc95.rickyandmorty.framework.network.service.CharacterService
import com.adrc95.rickyandmorty.framework.network.service.EpisodeService
import com.adrc95.rickyandmorty.framework.network.service.LocationService
import com.adrc95.rickyandmorty.framework.network.service.createCharacterService
import com.adrc95.rickyandmorty.framework.network.service.createEpisodeService
import com.adrc95.rickyandmorty.framework.network.service.createLocationService
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

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
    fun provideHttpClient(
        json: Json,
        cacheStorage: CacheStorage,
    ): HttpClient = HttpClient {
        expectSuccess = true
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpTimeout) {
            connectTimeoutMillis = CONNECT_TIMEOUT_SECONDS * 1_000
            requestTimeoutMillis = READ_TIMEOUT_SECONDS * 1_000
            socketTimeoutMillis = WRITE_TIMEOUT_SECONDS * 1_000
        }
        install(HttpCache) {
            publicStorage(cacheStorage)
            privateStorage(cacheStorage)
        }
    }

    @Single
    fun provideKtorFit(
        @Named("baseUrl") baseUrl: String,
        httpClient: HttpClient,
    ): Ktorfit = Ktorfit.Builder()
        .baseUrl(baseUrl)
        .httpClient(httpClient)
        .converterFactories(
            FlowConverterFactory(),
            ResponseConverterFactory(),
        )
        .build()

    @Single
    fun provideCharacterService(ktorfit: Ktorfit): CharacterService = ktorfit.createCharacterService()

    @Single
    fun provideLocationService(ktorfit: Ktorfit): LocationService = ktorfit.createLocationService()

    @Single
    fun provideEpisodeService(ktorfit: Ktorfit): EpisodeService = ktorfit.createEpisodeService()
}
