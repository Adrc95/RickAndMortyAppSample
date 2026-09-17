package com.adrc95.rickyandmorty.di

import com.adrc95.rickyandmorty.framework.network.service.CharacterService
import com.adrc95.rickyandmorty.framework.network.service.EpisodeService
import com.adrc95.rickyandmorty.framework.network.service.LocationService
import com.adrc95.rickyandmorty.framework.network.service.createCharacterService
import com.adrc95.rickyandmorty.framework.network.service.createEpisodeService
import com.adrc95.rickyandmorty.framework.network.service.createLocationService
import com.adrc95.rickyandmorty.mockwebserver.MockWebServerUrlHolder
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.FlowConverterFactory
import de.jensklingenberg.ktorfit.converter.ResponseConverterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkTestModule = module {
    single {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    single<HttpClient> {
        HttpClient(OkHttp) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(get<Json>())
            }
        }
    }
    single<Ktorfit> {
        Ktorfit.Builder()
            .baseUrl(MockWebServerUrlHolder.baseUrl)
            .httpClient(get<HttpClient>())
            .converterFactories(
                FlowConverterFactory(),
                ResponseConverterFactory(),
            )
            .build()
    }
    single<CharacterService> { get<Ktorfit>().createCharacterService() }
    single<LocationService> { get<Ktorfit>().createLocationService() }
    single<EpisodeService> { get<Ktorfit>().createEpisodeService() }
}
