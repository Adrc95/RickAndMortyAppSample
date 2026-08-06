package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsCharacterFavouriteUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    operator fun invoke(characterId: Int): Flow<Boolean> =
        characterRepository.isFavourite(characterId)
}
