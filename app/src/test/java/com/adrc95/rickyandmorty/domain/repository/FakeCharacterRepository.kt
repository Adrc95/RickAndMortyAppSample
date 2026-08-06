package com.adrc95.rickyandmorty.domain.repository

import androidx.paging.PagingData
import com.adrc95.rickyandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class FakeCharacterRepository : CharacterRepository {

    private val characters = MutableStateFlow<List<Character>>(emptyList())
    private val favourites = MutableStateFlow<Set<Int>>(emptySet())
    private var detailResult: Character? = null

    override fun getCharacters(): Flow<PagingData<Character>> = emptyFlow()

    override fun searchCharacters(
        name: String?,
        species: String?,
        gender: String?,
        status: String?,
    ): Flow<PagingData<Character>> = emptyFlow()

    override fun getCharacterDetail(id: Int): Flow<Character> =
        characters.asStateFlow().map { list ->
            detailResult ?: list.first { it.id == id }
        }

    override fun isFavourite(characterId: Int): Flow<Boolean> =
        favourites.asStateFlow().map { it.contains(characterId) }

    override suspend fun toggleFavourite(characterId: Int) {
        favourites.value = favourites.value.toMutableSet().apply {
            if (contains(characterId)) remove(characterId) else add(characterId)
        }
    }

    fun setCharacters(items: List<Character>) {
        characters.value = items
    }

    fun setDetailResult(character: Character?) {
        detailResult = character
    }

    fun setFavourite(characterId: Int, isFavourite: Boolean) {
        favourites.value = favourites.value.toMutableSet().apply {
            if (isFavourite) add(characterId) else remove(characterId)
        }
    }
}
