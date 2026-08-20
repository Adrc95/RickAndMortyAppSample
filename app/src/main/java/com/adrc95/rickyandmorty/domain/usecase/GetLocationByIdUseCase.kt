package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.repository.LocationRepository
import org.koin.core.annotation.Factory

@Factory
class GetLocationByIdUseCase(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(characterId: Int, locationId: Int, isOrigin: Boolean): Result<LocationDetail?> =
        locationRepository.getLocation(characterId, locationId, isOrigin)
}
