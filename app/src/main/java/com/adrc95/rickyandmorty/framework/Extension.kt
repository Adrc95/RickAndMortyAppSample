package com.adrc95.rickyandmorty.framework

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.adrc95.rickyandmorty.domain.exception.AppError
import okio.IOException
import retrofit2.HttpException
import com.adrc95.rickyandmorty.domain.exception.Result

suspend fun <T> tryCall(action: suspend () -> T): Result<T> = try {
    Result.Success(action.invoke())
} catch (e: Exception) {
    Result.Error(e.toError())
}

inline fun <reified T> MutablePreferences.set(
    key: String,
    value: T
) {
    this[preferenceKey<T>(key)] = value
}

inline fun <reified T> MutablePreferences.remove(key: String) {
    remove(preferenceKey<T>(key))
}

inline fun <reified T> Preferences.getOrDefault(key: String, default: T): T {
    val prefKey = preferenceKey<T>(key)
    return this[prefKey] ?: default
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> preferenceKey(name: String): Preferences.Key<T> =
    when (T::class) {
        Boolean::class -> booleanPreferencesKey(name) as Preferences.Key<T>
        Double::class -> doublePreferencesKey(name) as Preferences.Key<T>
        Float::class -> floatPreferencesKey(name) as Preferences.Key<T>
        Int::class -> intPreferencesKey(name) as Preferences.Key<T>
        Long::class -> longPreferencesKey(name) as Preferences.Key<T>
        Set::class -> stringSetPreferencesKey(name) as Preferences.Key<T>
        else -> stringPreferencesKey(name) as Preferences.Key<T>
    }


fun Throwable.toError(): AppError = when (this) {
    is IOException -> AppError.Connectivity
    is HttpException -> AppError.Server(code())
    else -> AppError.Unknown(message.orEmpty())
}
