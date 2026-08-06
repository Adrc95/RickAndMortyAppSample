package com.adrc95.rickyandmorty.domain.repository

import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.LocationDetail

interface LocationRepository {
    suspend fun getLocation(characterId: Int, locationId: Int, isOrigin: Boolean): Result<LocationDetail?>
}
