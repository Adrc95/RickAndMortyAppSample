package com.adrc95.rickyandmorty.di

import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@KoinApplication(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataBaseModule::class,
        DataStoreModule::class
    ]
)
@Module
class KoinApp
