package com.adrc95.rickyandmorty.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.PresentationConstants.MIN_SEARCH_LENGTH
import com.adrc95.rickyandmorty.presentation.core.composable.AppToolbar
import com.adrc95.rickyandmorty.presentation.core.composable.EmptyContent
import com.adrc95.rickyandmorty.presentation.core.composable.ErrorBanner
import com.adrc95.rickyandmorty.presentation.core.composable.LoadingContent
import com.adrc95.rickyandmorty.presentation.core.composable.SearchBar
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.FilterBottomSheet
import com.adrc95.rickyandmorty.presentation.home.composable.CharacterGrid

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onCharacterClick: (CharacterDisplayModel) -> Unit,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val characters = viewModel.characters.collectAsLazyPagingItems()
    HomeScreen(
        characters = characters,
        uiState = uiState,
        onCharacterClick = onCharacterClick,
        onFavouriteClick = viewModel::onToggleFavourite,
        onSettingsClick = onSettingsClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onApplyFilters = viewModel::onFiltersChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeViewModel.UiState,
    characters: LazyPagingItems<CharacterDisplayModel>,
    onCharacterClick: (CharacterDisplayModel) -> Unit,
    onFavouriteClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onApplyFilters: (CharacterFiltersDisplayModel) -> Unit
) {
    var showFilterSheet by remember { mutableStateOf(false) }

    if (showFilterSheet) {
        FilterBottomSheet(
            filterGroupDisplayModels = uiState.filterGroups,
            currentFilters = uiState.filters,
            onApply = { onApplyFilters(it) },
            onDismiss = { showFilterSheet = false }
        )
    }

    val hasActiveFilters =
        uiState.filters.species != null || uiState.filters.gender != null || uiState.filters.status != null
    val isInSearchMode = uiState.searchQuery.length >= MIN_SEARCH_LENGTH || hasActiveFilters

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                AppToolbar(
                    title = stringResource(R.string.app_name),
                    onActionClick = onSettingsClick
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        placeholder = stringResource(R.string.search_by_name_placeholder),
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilterChip(
                        selected = hasActiveFilters,
                        onClick = { showFilterSheet = true },
                        label = { Text(stringResource(R.string.filter)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.icon_filter),
                                contentDescription = null,
                                modifier = Modifier.width(18.dp)
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { padding ->
        HomeContent(
            modifier = Modifier.padding(padding),
            characters = characters,
            searchQuery = uiState.searchQuery,
            filters = uiState.filters,
            showFavourite = !isInSearchMode,
            isInSearchMode = isInSearchMode,
            onCharacterClick = onCharacterClick,
            onFavouriteClick = onFavouriteClick
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    characters: LazyPagingItems<CharacterDisplayModel>,
    filters: CharacterFiltersDisplayModel = CharacterFiltersDisplayModel(),
    showFavourite: Boolean = true,
    isInSearchMode: Boolean = false,
    onCharacterClick: (CharacterDisplayModel) -> Unit,
    onFavouriteClick: (Int) -> Unit
) {
    val refresh = characters.loadState.refresh
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(refresh) {
        if (isRefreshing && refresh !is LoadState.Loading) {
            isRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            characters.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CharacterGrid(
                modifier = Modifier.fillMaxSize(),
                characters = characters,
                onCharacterClick = onCharacterClick,
                onFavouriteClick = onFavouriteClick,
                showFavourite = showFavourite,
                searchQuery = searchQuery,
                filters = filters
            )

            if (refresh is LoadState.Loading && characters.itemCount == 0) {
                LoadingContent()
            }

            if (refresh is LoadState.Error) {
                ErrorBanner(
                    message = stringResource(R.string.error_connectivity),
                    onRetry = { characters.retry() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            val isNotLoading = refresh is LoadState.NotLoading
            if (isNotLoading && characters.itemCount == 0) {
                EmptyContent(
                    message = stringResource(
                        if (isInSearchMode) {
                            R.string.empty_search_results
                        } else {
                            R.string.empty_characters
                        }
                    )
                )
            }

            val appendState = characters.loadState.append
            if (appendState is LoadState.Error && characters.itemCount > 0) {
                ErrorBanner(
                    message = stringResource(R.string.error_load_characters),
                    onRetry = { characters.retry() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
