package com.adrc95.rickyandmorty.domain.repository

import com.adrc95.rickyandmorty.domain.exception.AppError
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.LocationDetail

class FakeLocationRepository : LocationRepository {

    private val locations = mutableMapOf<Pair<Int, Boolean>, LocationDetail>()
    private var error: AppError? = null

    override suspend fun getLocation(characterId: Int, locationId: Int, isOrigin: Boolean): Result<LocationDetail?> {
        error?.let { return Result.Error(it) }
        return Result.Success(locations[characterId to isOrigin])
    }

    fun setLocation(characterId: Int, isOrigin: Boolean, location: LocationDetail) {
        locations[characterId to isOrigin] = location
    }

    fun setError(error: AppError?) {
        this.error = error
    }
}
