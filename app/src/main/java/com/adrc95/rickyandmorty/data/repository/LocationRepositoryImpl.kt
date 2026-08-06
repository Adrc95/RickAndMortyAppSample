package com.adrc95.rickyandmorty.data.repository

import com.adrc95.rickyandmorty.data.datasource.LocalDataSource
import com.adrc95.rickyandmorty.data.datasource.RemoteDataSource
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : LocationRepository {

    override suspend fun getLocation(characterId: Int, locationId: Int, isOrigin: Boolean): Result<LocationDetail?> {
        val cached = localDataSource.getLocationByCharacterId(characterId, isOrigin)
        if (cached != null) return Result.Success(cached)

        return when (val result = remoteDataSource.getLocationById(locationId)) {
            is Result.Success -> {
                localDataSource.saveLocationDetail(result.data, characterId, isOrigin)
                Result.Success(result.data)
            }
            is Result.Error -> result
        }
    }
}
