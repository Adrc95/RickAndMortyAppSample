package com.adrc95.rickyandmorty.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.presentation.core.PresentationConstants.ANIMATION_DURATION_MILLIS
import com.adrc95.rickyandmorty.presentation.core.PresentationConstants.IMAGE_ASPECT_RATIO
import com.adrc95.rickyandmorty.presentation.detail.composable.CharacterSession
import com.adrc95.rickyandmorty.presentation.detail.composable.EpisodesSection
import com.adrc95.rickyandmorty.presentation.detail.composable.InfoGrid
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.mapper.toGenericMessage

@Composable
fun DetailRoute(
    viewModel: DetailViewModel,
    onSettingsClick: () -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    DetailScreen(
        state = uiState,
        onBack = onBack,
        onSettingsClick = onSettingsClick,
        onFavouriteClick = viewModel::onToggleFavourite,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    state: DetailViewModel.UiState,
    onBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onFavouriteClick: () -> Unit,
) {
    val scroll: ScrollState = rememberScrollState(0)

    val density = LocalDensity.current
    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp

    val imageHeight = screenWidth / IMAGE_ASPECT_RATIO

    val headerHeightPx = with(density) { imageHeight.toPx() }

    val scrollProgress by remember {
        derivedStateOf {
            (scroll.value.toFloat() / headerHeightPx).coerceIn(0f, 1f)
        }
    }

    val showToolbar by remember {
        derivedStateOf { scroll.value >= headerHeightPx }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = showToolbar,
                        enter = fadeIn(animationSpec = tween(ANIMATION_DURATION_MILLIS)),
                        exit = fadeOut(animationSpec = tween(ANIMATION_DURATION_MILLIS))
                    ) {
                        Text(
                            text = state.character?.name.orEmpty(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onSettingsClick() }) {
                        Icon(
                            painter = painterResource(R.drawable.icon_settings),
                            contentDescription = stringResource(R.string.settings),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = scrollProgress
                    ),
                    scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = scrollProgress
                    ),
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        }
    ) { padding ->
        when {
            state.isLoading && state.character == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    val loadingDescription = stringResource(R.string.loading)
                    CircularProgressIndicator(
                        modifier = Modifier.semantics {
                            contentDescription = loadingDescription
                        }
                    )
                }
            }

            state.error != null && state.character == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error.toGenericMessage(context),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                state.character?.let {
                    DetailContent(
                        scroll = scroll,
                        headerHeight = headerHeightPx,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(scroll),
                        character = it,
                        onFavouriteClick = onFavouriteClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    modifier: Modifier = Modifier,
    scroll: ScrollState,
    headerHeight: Float,
    character: CharacterDisplayModel,
    onFavouriteClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier,
    ) {
        CharacterSession(
            headerHeight = headerHeight,
            scroll = scroll,
            name = character.name,
            image = character.image,
            status = character.status,
            isFavourite = character.isFavourite,
            onFavouriteClick = onFavouriteClick,
        )
        InfoGrid(
            character = character,
        )
        EpisodesSection(
            episodes = character.episodeDetails,
        )
    }
}
