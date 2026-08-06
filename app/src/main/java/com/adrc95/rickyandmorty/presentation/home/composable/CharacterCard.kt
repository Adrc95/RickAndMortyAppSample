package com.adrc95.rickyandmorty.presentation.home.composable

import android.content.res.Configuration
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.TestTags.FAVOURITE_BUTTON
import com.adrc95.rickyandmorty.presentation.core.composable.AsyncCharacterImage
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.presentation.ui.theme.Shapes

@Composable
fun CharacterCard(
    modifier: Modifier = Modifier,
    character: CharacterDisplayModel,
    showFavourite: Boolean = true,
    onFavouriteClick: () -> Unit,
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
        shape = Shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncCharacterImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
                if (showFavourite) {
                    IconButton(
                        onClick = onFavouriteClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                            .size(48.dp)
                            .clip(CircleShape)
                            .testTag(FAVOURITE_BUTTON)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            painter = painterResource(
                                if (character.isFavourite) {
                                    R.drawable.icon_favourite_fill
                                }
                                else {
                                    R.drawable.icon_favourite
                                }
                            ),
                            contentDescription = stringResource(
                                if (character.isFavourite) {
                                    R.string.remove_favourite
                                } else {
                                    R.string.add_favourite
                                }
                            ),
                            tint = if (character.isFavourite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(character.status.color)
                    )
                    Text(
                        text = stringResource(character.status.text).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = character.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${character.species} \u2022 ${character.gender}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CharacterCardPreview() {
    RickyAndMortyTheme {
        CharacterCard(
            modifier = Modifier.width(170.dp),
            character = CharacterDisplayModel(
                id = 0,
                name = "Demo",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                status = CharacterStatusDisplayModel.DEAD,
                species = "Human",
                type = "",
                gender = "Male",
                origin = SummaryLocation(id = 1, name = "Earth"),
                originDetail = null,
                location = SummaryLocation(id = 2, name = "Mars"),
                locationDetail = null,
                episodeIds = emptyList(),
                isFavourite = false,
            ),
            onFavouriteClick = {},
        )
    }
}
