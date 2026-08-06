package com.adrc95.rickyandmorty.presentation.home.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel

@Composable
fun CharacterGrid(
    modifier: Modifier = Modifier,
    characters: LazyPagingItems<CharacterDisplayModel>,
    onCharacterClick: (CharacterDisplayModel) -> Unit,
    onFavouriteClick: (Int) -> Unit,
    showFavourite: Boolean = true,
    searchQuery: String = "",
    filters: CharacterFiltersDisplayModel = CharacterFiltersDisplayModel(),
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            gridState.scrollToItem(0)
        }
    }

    LaunchedEffect(filters) {
        if (filters.species != null || filters.gender != null || filters.status != null) {
            gridState.scrollToItem(0)
        }
    }
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        state = gridState,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Adaptive(170.dp)
    ) {
        items(
            count = characters.itemCount,
            key = characters.itemKey { it.id },
            contentType = characters.itemContentType()
        ) { index ->
            characters[index]?.let { character ->
                CharacterCard(
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClick = { onCharacterClick(character) }
                    ),
                    character = character,
                    showFavourite = showFavourite,
                    onFavouriteClick = { onFavouriteClick(character.id) }
                )
            }
        }
    }
}
