package com.adrc95.rickyandmorty.presentation.core.mapper

import androidx.compose.runtime.Composable
import com.adrc95.rickyandmorty.domain.exception.AppError
import org.jetbrains.compose.resources.stringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.error_connectivity
import com.adrc95.rickyandmorty.generated.resources.error_server
import com.adrc95.rickyandmorty.generated.resources.error_unknown

@Composable
fun AppError.toGenericMessage(): String = when (this) {
    is AppError.Connectivity -> stringResource(Res.string.error_connectivity)
    is AppError.Server -> stringResource(Res.string.error_server, code)
    is AppError.Unknown -> message.ifEmpty { stringResource(Res.string.error_unknown) }
}
