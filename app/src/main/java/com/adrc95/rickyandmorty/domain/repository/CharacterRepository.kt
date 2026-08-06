package com.adrc95.rickyandmorty.domain.repository

import androidx.paging.PagingData
import com.adrc95.rickyandmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(): Flow<PagingData<Character>>
    fun searchCharacters(name: String?, species: String?, gender: String?, status: String?): Flow<PagingData<Character>>
    fun getCharacterDetail(id: Int): Flow<Character>
    fun isFavourite(characterId: Int): Flow<Boolean>
    suspend fun toggleFavourite(characterId: Int)
}
