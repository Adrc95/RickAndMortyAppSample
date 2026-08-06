package com.adrc95.rickyandmorty.domain.usecase

import androidx.paging.PagingData
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(
        name: String?,
        species: String? = null,
        gender: String? = null,
        status: String? = null,
    ): Flow<PagingData<Character>> =
        characterRepository.searchCharacters(name, species, gender, status)
}
