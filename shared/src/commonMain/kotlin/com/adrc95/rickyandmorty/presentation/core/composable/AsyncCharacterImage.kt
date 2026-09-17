package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.image_load_error
import com.adrc95.rickyandmorty.generated.resources.placeholder_error

@Composable
fun AsyncCharacterImage(
    modifier: Modifier = Modifier,
    model: String,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A1C1E),
                                Color(0x001A1C1E)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = Color.Gray,
                    strokeWidth = 2.dp
                )
            }
        },
        error = {
            Image(
                painter = painterResource(Res.drawable.placeholder_error),
                contentDescription = stringResource(Res.string.image_load_error)
            )
        }
    )
}
