package com.adrc95.rickyandmorty.presentation.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.extension.string
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun givenLoadingState_whenScreenIsDisplayed_thenShowsProgressIndicator() {
        setContent(DetailViewModel.UiState(isLoading = true))
        composeTestRule.onNode(
            hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)
        ).assertIsDisplayed()
    }

    @Test
    fun givenErrorState_whenScreenIsDisplayed_thenShowsConnectivityMessage() {
        setContent(DetailViewModel.UiState(error = AppError.Connectivity))
        composeTestRule.onNodeWithText(R.string.error_connectivity.string()).assertIsDisplayed()
    }

    @Test
    fun givenLoadingStateWithCharacter_whenScreenIsDisplayed_thenShowsCharacterContent() {
        setContent(DetailViewModel.UiState(isLoading = true, character = character()))
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertDoesNotExist()
    }

    @Test
    fun givenErrorStateWithCharacter_whenScreenIsDisplayed_thenKeepsCharacterContent() {
        setContent(
            DetailViewModel.UiState(
                character = character(),
                error = AppError.Connectivity,
            )
        )
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText(R.string.error_connectivity.string()).assertDoesNotExist()
    }

    @Test
    fun givenCharacterState_whenScreenIsDisplayed_thenShowsCharacterInformation() {
        setContent(
            DetailViewModel.UiState(
                character = character(
                    episodeDetails = List(10) { index ->
                        EpisodeDetail(
                            id = index,
                            name = "Episode $index",
                            episode = "S01E${index.toString().padStart(2, '0')}",
                            airDate = "January 1, 2020",
                        )
                    }
                )
            )
        )
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("ALIVE").assertIsDisplayed()
        composeTestRule.onNodeWithText("Human / Male").assertIsDisplayed()
        composeTestRule.onNodeWithText("Earth (C-137)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Citadel of Ricks").assertExists()
    }

    @Test
    fun givenDeadCharacter_whenScreenIsDisplayed_thenShowsDeadStatus() {
        setContent(
            DetailViewModel.UiState(
                character = character(status = CharacterStatusDisplayModel.DEAD)
            )
        )
        composeTestRule.onNodeWithText(CharacterStatusDisplayModel.DEAD.text.string().uppercase())
            .assertIsDisplayed()
    }
    @Test
    fun givenUnknownCharacterStatus_whenScreenIsDisplayed_thenShowsUnknownStatus() {
        setContent(
            DetailViewModel.UiState(
                character = character(status = CharacterStatusDisplayModel.UNKNOWN)
            )
        )
        composeTestRule
            .onNodeWithText(CharacterStatusDisplayModel.UNKNOWN.text.string().uppercase())
            .assertIsDisplayed()
    }

    @Test
    fun givenCharacterWithoutLocationDetails_whenScreenIsDisplayed_thenShowsFallbackValues() {
        setContent(DetailViewModel.UiState(character = character()))
        composeTestRule.onNodeWithText(R.string.origin.string().uppercase()).assertExists()
        composeTestRule.onNodeWithText(R.string.last_seen.string().uppercase()).assertExists()
        composeTestRule.onAllNodesWithText("-").assertCountEquals(6)
    }

    @Test
    fun givenCharacterWithLocationDetails_whenScreenIsDisplayed_thenShowsLocationDetails() {
        setContent(
            DetailViewModel.UiState(
                character = character(
                    originDetail = LocationDetail(
                        id = 1,
                        name = "Earth (C-137)",
                        type = "Planet",
                        dimension = "Dimension C-137",
                        residentsCount = 10,
                    ),
                    locationDetail = LocationDetail(
                        id = 3,
                        name = "Citadel of Ricks",
                        type = "Space station",
                        dimension = "unknown",
                        residentsCount = 20,
                    ),
                )
            )
        )

        composeTestRule.onNodeWithText("Planet").assertExists()
        composeTestRule.onNodeWithText("Dimension C-137").assertExists()
        composeTestRule.onNodeWithText("10").assertExists()
        composeTestRule.onNodeWithText("Space station").assertExists()
        composeTestRule.onNodeWithText("unknown").assertExists()
        composeTestRule.onNodeWithText("20").assertExists()
    }

    @Test
    fun givenCharacterWithEpisodes_whenScreenIsDisplayed_thenShowsEpisodesSection() {
        setContent(
            DetailViewModel.UiState(
                character = character(
                    episodeDetails = listOf(
                        EpisodeDetail(
                            id = 1,
                            name = "Pilot",
                            episode = "S01E01",
                            airDate = "December 2, 2013",
                        )
                    )
                )
            )
        )
        composeTestRule.onNodeWithText(R.string.appears_in.string()).assertExists()
        composeTestRule.onNodeWithText("S01E01").assertExists()
        composeTestRule.onNodeWithText("Pilot").assertExists()
    }

    @Test
    fun givenCharacterState_whenActionsAreClicked_thenEmitsCallbacks() {
        var backClicked = false
        var settingsClicked = false
        var favouriteClicked = false
        setContent(
            state = DetailViewModel.UiState(character = character()),
            onBack = { backClicked = true },
            onSettingsClick = { settingsClicked = true },
            onFavouriteClick = { favouriteClicked = true },
        )
        composeTestRule.onNodeWithContentDescription(R.string.back.string()).performClick()
        composeTestRule.onNodeWithContentDescription(R.string.settings.string()).performClick()
        composeTestRule.onNodeWithContentDescription(R.string.favourite.string()).performClick()
        assertTrue(backClicked)
        assertTrue(settingsClicked)
        assertTrue(favouriteClicked)
    }

    @Test
    fun givenCharacterState_whenContentIsScrolled_thenShowsCharacterNameInToolbar() {
        setContent(DetailViewModel.UiState(character = character()))
        repeat(3) {
            composeTestRule.onRoot().performTouchInput { swipeUp() }
        }
        composeTestRule.mainClock.advanceTimeBy(500)
        composeTestRule.onAllNodesWithText("Rick Sanchez").assertCountEquals(1)
    }

    private fun setContent(
        state: DetailViewModel.UiState,
        onBack: () -> Unit = {},
        onSettingsClick: () -> Unit = {},
        onFavouriteClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            RickyAndMortyTheme {
                DetailScreen(
                    state = state,
                    onBack = onBack,
                    onSettingsClick = onSettingsClick,
                    onFavouriteClick = onFavouriteClick,
                )
            }
        }
    }

    private fun character(
        status: CharacterStatusDisplayModel = CharacterStatusDisplayModel.ALIVE,
        originDetail: LocationDetail? = null,
        locationDetail: LocationDetail? = null,
        episodeDetails: List<EpisodeDetail> = emptyList(),
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
        episodeDetails = episodeDetails,
    )
}
