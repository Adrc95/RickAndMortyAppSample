package com.adrc95.rickyandmorty.presentation.home

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.collectAsLazyPagingItems
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.presentation.core.TestTags.FAVOURITE_BUTTON
import com.adrc95.rickyandmorty.presentation.core.TestTags.SETTINGS_BUTTON
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterFiltersDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import com.adrc95.rickyandmorty.presentation.ui.theme.RickyAndMortyTheme
import com.adrc95.rickyandmorty.testing.resolve
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.app_name
import com.adrc95.rickyandmorty.generated.resources.empty_characters
import com.adrc95.rickyandmorty.generated.resources.filter

@OptIn(ExperimentalTestApi::class)
class HomeScreenTest {

    @Test
    fun givenCharacters_whenScreenIsDisplayed_thenShowsCharacterAndControls() = runComposeUiTest {
        showHome()
        onNodeWithText(Res.string.app_name.resolve()).assertIsDisplayed()
        onNodeWithText(Res.string.filter.resolve()).assertIsDisplayed()
        onNodeWithText("Rick Sanchez").assertIsDisplayed()
        onNodeWithText("Human • Male").assertIsDisplayed()
        onNodeWithText("ALIVE").assertIsDisplayed()
    }

    @Test
    fun whenCharacterAndFavouriteAreClicked_thenEmitsCallbacks() = runComposeUiTest {
        var selectedCharacter: CharacterDisplayModel? = null
        var favouriteCharacterId: Int? = null
        showHome(
            onCharacterClick = { selectedCharacter = it },
            onFavouriteClick = { favouriteCharacterId = it }
        )
        onNodeWithText("Rick Sanchez").performClick()
        onNodeWithTag(FAVOURITE_BUTTON).performClick()
        assertEquals(character(), selectedCharacter)
        assertEquals(1, favouriteCharacterId)
    }

    @Test
    fun whenSearchQueryChanges_thenEmitsNewQuery() = runComposeUiTest {
        var query = ""
        showHome(onSearchQueryChange = { query = it })
        onNode(hasSetTextAction()).performTextInput("rick")
        assertEquals("rick", query)
    }

    @Test
    fun whenSettingsIsClicked_thenEmitsCallback() = runComposeUiTest {
        var settingsClicked = false
        showHome(onSettingsClick = { settingsClicked = true })
        onNodeWithTag(SETTINGS_BUTTON).performClick()
        assertEquals(true, settingsClicked)
    }

    @Test
    fun givenSearchMode_whenScreenIsDisplayed_thenHidesFavouriteAction() = runComposeUiTest {
        showHome(uiState = HomeViewModel.UiState(searchQuery = "rick"))
        onNodeWithText("Rick Sanchez").assertIsDisplayed()
        onNodeWithTag(FAVOURITE_BUTTON).assertDoesNotExist()
    }

    @Test
    fun givenNoCharacters_whenScreenIsDisplayed_thenShowsEmptyMessage() = runComposeUiTest {
        showHome(characters = emptyCharacters())
        onNodeWithText(Res.string.empty_characters.resolve()).assertIsDisplayed()
    }

    private fun ComposeUiTest.showHome(
        uiState: HomeViewModel.UiState = HomeViewModel.UiState(),
        characters: Flow<PagingData<CharacterDisplayModel>> =
            flowOf(PagingData.from(listOf(character()))),
        onCharacterClick: (CharacterDisplayModel) -> Unit = {},
        onFavouriteClick: (Int) -> Unit = {},
        onSearchQueryChange: (String) -> Unit = {},
        onSettingsClick: () -> Unit = {},
        onApplyFilters: (CharacterFiltersDisplayModel) -> Unit = {}
    ) {
        setContent {
            RickyAndMortyTheme {
                val lazyCharacters = characters.collectAsLazyPagingItems()
                HomeScreen(
                    uiState = uiState,
                    characters = lazyCharacters,
                    onCharacterClick = onCharacterClick,
                    onFavouriteClick = onFavouriteClick,
                    onSettingsClick = onSettingsClick,
                    onSearchQueryChange = onSearchQueryChange,
                    onApplyFilters = onApplyFilters
                )
            }
        }
        waitForIdle()
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
        episodeDetails = emptyList()
    )

    private fun emptyCharacters(): Flow<PagingData<CharacterDisplayModel>> = Pager(PagingConfig(pageSize = 20)) {
        object : PagingSource<Int, CharacterDisplayModel>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDisplayModel> =
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )

            override fun getRefreshKey(state: PagingState<Int, CharacterDisplayModel>): Int? = null
        }
    }.flow
}
