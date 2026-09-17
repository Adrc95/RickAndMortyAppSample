package com.adrc95.rickyandmorty.di

import coil3.ImageLoader
import coil3.SingletonImageLoader
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

@Module
@ComponentScan("com.adrc95")
class AppModule

@KoinApplication(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataBaseModule::class,
    ]
)
object RickyAndMortyApp

fun initKoin(config: KoinAppDeclaration? = null) {
    Napier.base(DebugAntilog())
    val koinApplication = startKoin<RickyAndMortyApp> {
        config?.invoke(this)
    }
    SingletonImageLoader.setSafe { koinApplication.koin.get<ImageLoader>() }
}
