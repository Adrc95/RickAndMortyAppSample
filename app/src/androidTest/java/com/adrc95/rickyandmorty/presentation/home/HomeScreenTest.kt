package com.adrc95.rickyandmorty.presentation.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasSetTextAction
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.PagingConfig
import androidx.paging.Pager
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrc95.rickyandmorty.R
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.extension.string
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun givenCharacters_whenScreenIsDisplayed_thenShowsCharacterAndControls() {
        setContent()
        composeTestRule.onNodeWithText(R.string.app_name.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText(R.string.filter.string()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithText("Human • Male").assertIsDisplayed()
        composeTestRule.onNodeWithText("ALIVE").assertIsDisplayed()
    }

    @Test
    fun whenCharacterAndFavouriteAreClicked_thenEmitsCallbacks() {
        var selectedCharacter: CharacterDisplayModel? = null
        var favouriteCharacterId: Int? = null
        setContent(
            onCharacterClick = { selectedCharacter = it },
            onFavouriteClick = { favouriteCharacterId = it },
        )
        composeTestRule.onNodeWithText("Rick Sanchez").performClick()
        composeTestRule.onNodeWithTag("favourite_button").performClick()
        assertEquals(character(), selectedCharacter)
        assertEquals(1, favouriteCharacterId)
    }

    @Test
    fun whenSearchQueryChanges_thenEmitsNewQuery() {
        var query = ""
        setContent(onSearchQueryChange = { query = it })
        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("rick")
        assertEquals("rick", query)
    }

    @Test
    fun whenSettingsIsClicked_thenEmitsCallback() {
        var settingsClicked = false
        setContent(onSettingsClick = { settingsClicked = true })
        composeTestRule.onNodeWithTag("settings_button").performClick()
        assertEquals(true, settingsClicked)
    }

    @Test
    fun givenSearchMode_whenScreenIsDisplayed_thenHidesFavouriteAction() {
        setContent(uiState = HomeViewModel.UiState(searchQuery = "rick"))
        composeTestRule.onNodeWithText("Rick Sanchez").assertIsDisplayed()
        composeTestRule.onNodeWithTag("favourite_button").assertDoesNotExist()
    }

    @Test
    fun givenNoCharacters_whenScreenIsDisplayed_thenShowsEmptyMessage() {
        setContent(characters = emptyCharacters())
        composeTestRule.onNodeWithText(R.string.empty_characters.string()).assertIsDisplayed()
    }

    private fun setContent(
        uiState: HomeViewModel.UiState = HomeViewModel.UiState(),
        characters: Flow<PagingData<CharacterDisplayModel>> =
            flowOf(PagingData.from(listOf(character()))),
        onCharacterClick: (CharacterDisplayModel) -> Unit = {},
        onFavouriteClick: (Int) -> Unit = {},
        onSearchQueryChange: (String) -> Unit = {},
        onSettingsClick: () -> Unit = {},
        onApplyFilters: (CharacterFiltersDisplayModel) -> Unit = {},
    ) {
        composeTestRule.setContent {
            RickyAndMortyTheme {
                val lazyCharacters = characters.collectAsLazyPagingItems()
                HomeScreen(
                    uiState = uiState,
                    characters = lazyCharacters,
                    onCharacterClick = onCharacterClick,
                    onFavouriteClick = onFavouriteClick,
                    onSettingsClick = onSettingsClick,
                    onSearchQueryChange = onSearchQueryChange,
                    onApplyFilters = onApplyFilters,
                )
            }
        }
        composeTestRule.waitForIdle()
    }

    private fun character() = CharacterDisplayModel(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatusDisplayModel.ALIVE,
        species = "Human",
        type = "",
        gender = "Male",
        origin = SummaryLocation(id = 1, name = "Earth"),
        originDetail = null,
        location = SummaryLocation(id = 3, name = "Citadel of Ricks"),
        locationDetail = null,
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episodeIds = emptyList(),
        episodeDetails = emptyList(),
    )

    private fun emptyCharacters(): Flow<PagingData<CharacterDisplayModel>> =
        Pager(PagingConfig(pageSize = 20)) {
            object : PagingSource<Int, CharacterDisplayModel>() {
                override suspend fun load(
                    params: LoadParams<Int>,
                ): LoadResult<Int, CharacterDisplayModel> =
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null,
                    )

                override fun getRefreshKey(
                    state: PagingState<Int, CharacterDisplayModel>,
                ): Int? = null
            }
        }.flow

}
