package com.adrc95.rickyandmorty.presentation.core.mapper

import android.content.Context
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.exception.AppError
import kotlin.text.ifEmpty

fun AppError.toGenericMessage(
    context: Context
): String = when (this) {
    is AppError.Connectivity -> context.getString(R.string.error_connectivity)
    is AppError.Server -> context.getString(R.string.error_server, code)
    is AppError.Unknown -> message.ifEmpty { context.getString(R.string.error_unknown) }
}