package com.adrc95.rickyandmorty.domain.exception

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
}
