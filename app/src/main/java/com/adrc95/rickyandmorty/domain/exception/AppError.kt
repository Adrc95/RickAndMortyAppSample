package com.adrc95.rickyandmorty.domain.exception

sealed class AppError {
    object Connectivity : AppError()
    data class Server(val code: Int) : AppError()
    data class Unknown(val message: String) : AppError()
}
