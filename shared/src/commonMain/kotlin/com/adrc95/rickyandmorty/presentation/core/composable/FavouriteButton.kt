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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.add_favourite
import com.adrc95.rickyandmorty.generated.resources.icon_favourite
import com.adrc95.rickyandmorty.generated.resources.icon_favourite_fill
import com.adrc95.rickyandmorty.generated.resources.remove_favourite

@Composable
fun FavouriteButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean, onClick: () -> Unit,
    size: Dp = 48.dp
) {
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
                    Res.drawable.icon_favourite_fill
                } else {
                    Res.drawable.icon_favourite
                }
            ),
            contentDescription = stringResource(
                if (isFavourite) Res.string.remove_favourite else Res.string.add_favourite
            ),
            tint = if (isFavourite) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}
