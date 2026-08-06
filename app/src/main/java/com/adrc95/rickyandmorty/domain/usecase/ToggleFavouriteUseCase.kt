package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val characterRepository: CharacterRepository,
) {
    suspend operator fun invoke(characterId: Int) {
        characterRepository.toggleFavourite(characterId)
    }
}
