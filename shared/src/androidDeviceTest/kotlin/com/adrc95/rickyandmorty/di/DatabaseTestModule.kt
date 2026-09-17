package com.adrc95.rickyandmorty.di

import android.content.Context
import androidx.room.Room
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import org.koin.dsl.module

val databaseTestModule = module {
    single<AppDatabase> {
        Room.inMemoryDatabaseBuilder(get<Context>(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
    single<CharacterDao> { get<AppDatabase>().characterDao() }
    single<RemoteKeyDao> { get<AppDatabase>().remoteKeyDao() }
    single<LocationDetailDao> { get<AppDatabase>().locationDetailDao() }
    single<EpisodeDetailDao> { get<AppDatabase>().episodeDetailDao() }
}
