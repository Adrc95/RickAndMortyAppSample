package com.adrc95.rickyandmorty

import android.app.Application
import com.adrc95.rickyandmorty.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
        }
    }
}
