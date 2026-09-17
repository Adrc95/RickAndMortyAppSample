package com.adrc95.rickyandmorty.framework.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.adrc95.rickyandmorty.framework.database.DatabaseConstants.DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory).path!!
}

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = "${fileDirectory()}/${DATABASE_NAME}"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
}