package com.adrc95.rickyandmorty.framework.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.DATABASE_NAME

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = ctx.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}