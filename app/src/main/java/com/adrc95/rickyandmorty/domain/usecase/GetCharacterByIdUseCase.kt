package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCharacterByIdUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
    operator fun invoke(id: Int): Flow<Character> = characterRepository.getCharacterDetail(id)
}
