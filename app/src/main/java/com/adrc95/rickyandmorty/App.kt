package com.adrc95.rickyandmorty

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.adrc95.rickyandmorty.di.KoinApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.plugin.module.dsl.startKoin
import timber.log.Timber

class App :
    Application(),
    KoinComponent {

    override fun onCreate() {
        super.onCreate()
        startKoin<KoinApp> {
            androidContext(this@App)
        }
        val imageLoader: ImageLoader = get()
        SingletonImageLoader.setSafe { imageLoader }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
