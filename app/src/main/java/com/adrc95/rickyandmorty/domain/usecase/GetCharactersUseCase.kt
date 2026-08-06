package com.adrc95.rickyandmorty.domain.usecase

import androidx.paging.PagingData
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
    operator fun invoke(): Flow<PagingData<Character>> = characterRepository.getCharacters()
}
