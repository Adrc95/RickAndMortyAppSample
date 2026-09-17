package com.adrc95.rickyandmorty.presentation.detail

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.test.swipeUp
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.resolve
import kotlin.test.Test
import kotlin.test.assertTrue
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.add_favourite
import com.adrc95.rickyandmorty.generated.resources.appears_in
import com.adrc95.rickyandmorty.generated.resources.back
import com.adrc95.rickyandmorty.generated.resources.error_connectivity
import com.adrc95.rickyandmorty.generated.resources.last_seen
import com.adrc95.rickyandmorty.generated.resources.origin
import com.adrc95.rickyandmorty.generated.resources.settings

@OptIn(ExperimentalTestApi::class)
class DetailScreenTest {

    @Test
    fun givenLoadingState_whenScreenIsDisplayed_thenShowsProgressIndicator() = runComposeUiTest {
        showDetail(DetailViewModel.UiState(isLoading = true))
        onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
        ).assertIsDisplayed()
    }

    @Test
    fun givenErrorState_whenScreenIsDisplayed_thenShowsConnectivityMessage() = runComposeUiTest {
        showDetail(DetailViewModel.UiState(error = AppError.Connectivity))
        onNodeWithText(Res.string.error_connectivity.resolve()).assertIsDisplayed()
    }

    @Test
    fun givenLoadingStateWithCharacter_whenScreenIsDisplayed_thenShowsCharacterContent() = runComposeUiTest {
        showDetail(DetailViewModel.UiState(isLoading = true, character = character()))
        onNodeWithText("Rick Sanchez").assertIsDisplayed()
        onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertDoesNotExist()
    }

    @Test
    fun givenErrorStateWithCharacter_whenScreenIsDisplayed_thenKeepsCharacterContent() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(),
                error = AppError.Connectivity
            )
        )
        onNodeWithText("Rick Sanchez").assertIsDisplayed()
        onNodeWithText(Res.string.error_connectivity.resolve()).assertDoesNotExist()
    }

    @Test
    fun givenCharacterState_whenScreenIsDisplayed_thenShowsCharacterInformation() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(
                    episodeDetails = List(10) { index ->
                        EpisodeDetail(
                            id = index,
                            name = "Episode $index",
                            episode = "S01E${index.toString().padStart(2, '0')}",
                            airDate = "January 1, 2020"
                        )
                    }
                )
            )
        )
        onNodeWithText("Rick Sanchez").assertIsDisplayed()
        onNodeWithText("ALIVE").assertIsDisplayed()
        onNodeWithText("Human / Male").assertIsDisplayed()
        onNodeWithText("Earth (C-137)").performScrollTo().assertIsDisplayed()
        onNodeWithText("Citadel of Ricks").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun givenDeadCharacter_whenScreenIsDisplayed_thenShowsDeadStatus() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(status = CharacterStatusDisplayModel.DEAD)
            )
        )
        onNodeWithText(CharacterStatusDisplayModel.DEAD.text.resolve().uppercase())
            .assertIsDisplayed()
    }

    @Test
    fun givenUnknownCharacterStatus_whenScreenIsDisplayed_thenShowsUnknownStatus() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(status = CharacterStatusDisplayModel.UNKNOWN)
            )
        )
        onNodeWithText(CharacterStatusDisplayModel.UNKNOWN.text.resolve().uppercase())
            .assertIsDisplayed()
    }

    @Test
    fun givenCharacterWithoutLocationDetails_whenScreenIsDisplayed_thenShowsFallbackValues() = runComposeUiTest {
        showDetail(DetailViewModel.UiState(character = character()))
        onNodeWithText(Res.string.origin.resolve().uppercase()).assertExists()
        onNodeWithText(Res.string.last_seen.resolve().uppercase()).assertExists()
        onAllNodesWithText("-").assertCountEquals(6)
    }

    @Test
    fun givenCharacterWithLocationDetails_whenScreenIsDisplayed_thenShowsLocationDetails() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(
                    originDetail = LocationDetail(
                        id = 1,
                        name = "Earth (C-137)",
                        type = "Planet",
                        dimension = "Dimension C-137",
                        residentsCount = 10
                    ),
                    locationDetail = LocationDetail(
                        id = 3,
                        name = "Citadel of Ricks",
                        type = "Space station",
                        dimension = "unknown",
                        residentsCount = 20
                    )
                )
            )
        )

        onNodeWithText("Planet").assertExists()
        onNodeWithText("Dimension C-137").assertExists()
        onNodeWithText("10").assertExists()
        onNodeWithText("Space station").assertExists()
        onNodeWithText("unknown").assertExists()
        onNodeWithText("20").assertExists()
    }

    @Test
    fun givenCharacterWithEpisodes_whenScreenIsDisplayed_thenShowsEpisodesSection() = runComposeUiTest {
        showDetail(
            DetailViewModel.UiState(
                character = character(
                    episodeDetails = listOf(
                        EpisodeDetail(
                            id = 1,
                            name = "Pilot",
                            episode = "S01E01",
                            airDate = "December 2, 2013"
                        )
                    )
                )
            )
        )
        onNodeWithText(Res.string.appears_in.resolve()).assertExists()
        onNodeWithText("S01E01").assertExists()
        onNodeWithText("Pilot").assertExists()
    }

    @Test
    fun givenCharacterState_whenActionsAreClicked_thenEmitsCallbacks() = runComposeUiTest {
        var backClicked = false
        var settingsClicked = false
        var favouriteClicked = false
        showDetail(
            state = DetailViewModel.UiState(character = character()),
            onBack = { backClicked = true },
            onSettingsClick = { settingsClicked = true },
            onFavouriteClick = { favouriteClicked = true }
        )
        onNodeWithContentDescription(Res.string.back.resolve()).performClick()
        onNodeWithContentDescription(Res.string.settings.resolve()).performClick()
        onNodeWithContentDescription(Res.string.add_favourite.resolve()).performClick()
        assertTrue(backClicked)
        assertTrue(settingsClicked)
        assertTrue(favouriteClicked)
    }

    @Test
    fun givenCharacterState_whenContentIsScrolled_thenShowsCharacterNameInToolbar() = runComposeUiTest {
        showDetail(DetailViewModel.UiState(character = character()))
        repeat(3) {
            onRoot().performTouchInput { swipeUp() }
        }
        mainClock.advanceTimeBy(500)
        onAllNodesWithText("Rick Sanchez").assertCountEquals(1)
    }

    private fun ComposeUiTest.showDetail(
        state: DetailViewModel.UiState,
        onBack: () -> Unit = {},
        onSettingsClick: () -> Unit = {},
        onFavouriteClick: () -> Unit = {}
    ) {
        setContent {
            RickyAndMortyTheme {
                DetailScreen(
                    state = state,
                    onBack = onBack,
                    onSettingsClick = onSettingsClick,
                    onFavouriteClick = onFavouriteClick
                )
            }
        }
    }

    private fun character(
        status: CharacterStatusDisplayModel = CharacterStatusDisplayModel.ALIVE,
        originDetail: LocationDetail? = null,
        locationDetail: LocationDetail? = null,
        episodeDetails: List<EpisodeDetail> = emptyList()
    ) = CharacterDisplayModel(
        id = 1,
        name = "Rick Sanchez",
        status = status,
        species = "Human",
        type = "",
        gender = "Male",
        origin = SummaryLocation(id = 1, name = "Earth (C-137)"),
        originDetail = originDetail,
        location = SummaryLocation(id = 3, name = "Citadel of Ricks"),
        locationDetail = locationDetail,
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episodeIds = emptyList(),
        episodeDetails = episodeDetails
    )
}
