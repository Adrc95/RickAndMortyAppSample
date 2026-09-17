package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import org.jetbrains.compose.resources.stringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.loading

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(Res.string.loading)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics {
                contentDescription = loadingDescription
            }
        )
    }
}
