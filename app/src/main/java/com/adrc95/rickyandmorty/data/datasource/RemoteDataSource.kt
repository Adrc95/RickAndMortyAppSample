package com.adrc95.rickyandmorty.data.datasource

import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.Page

interface RemoteDataSource {
    suspend fun getCharacterById(id: Int): Result<Character>
    suspend fun getCharacters(page: Int): Result<Page<Character>>
    suspend fun searchCharacters(
        page: Int,
        name: String?,
        species: String?,
        gender: String?,
        status: String?
    ): Result<Page<Character>>
    suspend fun getLocationById(id: Int): Result<LocationDetail>
    suspend fun getEpisodesByIds(ids: List<Int>): Result<List<EpisodeDetail>>
}
