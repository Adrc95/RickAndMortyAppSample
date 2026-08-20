package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.repository.CharacterRepository
import org.koin.core.annotation.Factory

@Factory
class ToggleFavouriteUseCase(private val characterRepository: CharacterRepository) {
    suspend operator fun invoke(characterId: Int) {
        characterRepository.toggleFavourite(characterId)
    }
}
