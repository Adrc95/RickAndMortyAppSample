package com.adrc95.rickyandmorty.domain.usecase

import androidx.paging.PagingData
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetCharactersUseCase(private val characterRepository: CharacterRepository) {
    operator fun invoke(): Flow<PagingData<Character>> = characterRepository.getCharacters()
}
