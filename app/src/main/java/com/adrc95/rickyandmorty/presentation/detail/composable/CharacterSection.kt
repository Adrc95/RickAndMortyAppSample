package com.adrc95.rickyandmorty.presentation.detail.composable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.composable.AsyncCharacterImage
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel

@Composable
fun CharacterSession(
    scroll: ScrollState,
    headerHeight: Float,
    name: String,
    image: String,
    status: CharacterStatusDisplayModel,
    isFavourite: Boolean = false,
    onFavouriteClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .graphicsLayer { alpha = (-1f / headerHeight) * scroll.value + 1 }
            .fillMaxWidth()
            .aspectRatio(0.88f)
    ) {
        AsyncCharacterImage(
            model = image,
            contentDescription = name,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )
        StatusBadge(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    vertical = 24.dp,
                    horizontal = 16.dp,
                ),
            status = status
        )
        IconButton(
            onClick = onFavouriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    vertical = 24.dp,
                    horizontal = 16.dp,
                )
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        ) {
            Icon(
                painter = painterResource(
                    if (isFavourite) {
                        R.drawable.icon_favourite_fill
                    }
                    else {
                        R.drawable.icon_favourite
                    }
                ),
                contentDescription = stringResource(
                    if (isFavourite) R.string.remove_favourite else R.string.add_favourite
                ),
                tint = if (isFavourite) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    vertical = 24.dp,
                    horizontal = 16.dp,
                )
        )
    }
}

@Composable
private fun StatusBadge(
    modifier: Modifier = Modifier,
    status: CharacterStatusDisplayModel,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(status.color)
        )
        Text(
            text = stringResource(status.text).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
