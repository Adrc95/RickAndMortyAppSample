package com.adrc95.rickyandmorty.presentation.core.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R

@Composable
fun FavouriteButton(isFavourite: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier, size: Dp = 48.dp) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
    ) {
        Icon(
            painter = painterResource(
                if (isFavourite) {
                    R.drawable.icon_favourite_fill
                } else {
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
}
