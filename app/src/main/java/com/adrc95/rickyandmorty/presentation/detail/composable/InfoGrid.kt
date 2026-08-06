package com.adrc95.rickyandmorty.presentation.detail.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes


@Composable
fun InfoGrid(
    character: CharacterDisplayModel
) {
    Column(
        modifier = Modifier.padding(
            horizontal = 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.icon_gender,
            label = stringResource(R.string.specie_gender),
            value = "${character.species} / ${character.gender}",
        )
        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.icon_world,
            label = stringResource(R.string.origin),
            moreInfo = true,
            value = character.origin.name,
            type = character.originDetail?.type,
            dimension = character.originDetail?.dimension,
            residentsCount = character.originDetail?.residentsCount,
        )

        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            icon = R.drawable.icon_location,
            label = stringResource(R.string.last_seen),
            moreInfo = true,
            value = character.location.name,
            type = character.locationDetail?.type,
            dimension = character.locationDetail?.dimension,
            residentsCount = character.locationDetail?.residentsCount,
        )
    }
}


@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    label: String,
    value: String,
    moreInfo: Boolean = false,
    type: String? = null,
    dimension: String? = null,
    residentsCount: Int? = null,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        ),
        shape = Shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
            )
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (moreInfo) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                MoreInfo(
                    type = type,
                    dimension = dimension,
                    residentsCount = residentsCount,
                )
            }
        }
    }
}


@Composable
private fun MoreInfo(
    type: String? = null,
    dimension: String? = null,
    residentsCount: Int? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier =  Modifier.fillMaxWidth(),
    ) {
        MoreInfoItem(
            option = stringResource(R.string.type),
            text = type ?: "-",
            badge = false,
        )
        MoreInfoItem(
            option = stringResource(R.string.dimensions),
            text = dimension ?: "-",
            badge = false,
        )
        MoreInfoItem(
            option = stringResource(R.string.residents),
            text = residentsCount?.toString() ?: "-",
            badge = true,
        )
    }
}

@Composable
private fun MoreInfoItem(
    modifier: Modifier = Modifier,
    option: String,
    text: String,
    badge: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = option.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (badge) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}