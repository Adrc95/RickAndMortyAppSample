package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetCharacterByIdUseCase(private val characterRepository: CharacterRepository) {
    operator fun invoke(id: Int): Flow<Character> = characterRepository.getCharacterDetail(id)
}
