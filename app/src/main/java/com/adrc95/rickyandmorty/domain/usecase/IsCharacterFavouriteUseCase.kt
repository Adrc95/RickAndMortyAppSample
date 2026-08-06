package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsCharacterFavouriteUseCase @Inject constructor(private val characterRepository: CharacterRepository) {
    operator fun invoke(characterId: Int): Flow<Boolean> = characterRepository.isFavourite(characterId)
}
