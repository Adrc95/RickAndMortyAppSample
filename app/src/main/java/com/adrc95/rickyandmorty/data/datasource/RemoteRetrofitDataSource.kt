package com.adrc95.rickyandmorty.data.datasource

import com.adrc95.rickyandmorty.data.DataConstants.AMPERSAND_DELIMITER
import com.adrc95.rickyandmorty.data.DataConstants.COMMA_DELIMITER
import com.adrc95.rickyandmorty.data.DataConstants.PAGE_DELIMITER
import com.adrc95.rickyandmorty.domain.exception.Result
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.Page
import com.adrc95.rickyandmorty.framework.network.mapper.toDomain
import com.adrc95.rickyandmorty.framework.network.service.CharacterService
import com.adrc95.rickyandmorty.framework.network.service.EpisodeService
import com.adrc95.rickyandmorty.framework.network.service.LocationService
import com.adrc95.rickyandmorty.framework.tryCall
import javax.inject.Inject

class RemoteRetrofitDataSource @Inject constructor(
    private val characterService: CharacterService,
    private val locationService: LocationService,
    private val episodeService: EpisodeService
) : RemoteDataSource {

    override suspend fun getCharacterById(id: Int): Result<Character> = tryCall {
        characterService.getCharacterDetailById(id).toDomain()
    }

    override suspend fun getCharacters(page: Int): Result<Page<Character>> = tryCall {
        val result = characterService.getCharacters(page)
        Page(
            data = result.results.map { it.toDomain() },
            nextPage = result.info.next
                ?.substringAfter(PAGE_DELIMITER)
                ?.substringBefore(AMPERSAND_DELIMITER)
                ?.toIntOrNull()
        )
    }

    override suspend fun searchCharacters(
        page: Int,
        name: String?,
        species: String?,
        gender: String?,
        status: String?
    ): Result<Page<Character>> = tryCall {
        val result = characterService.getCharacters(page, name, species, gender, status)
        Page(
            data = result.results.map { it.toDomain() },
            nextPage = result.info.next
                ?.substringAfter(PAGE_DELIMITER)
                ?.substringBefore(AMPERSAND_DELIMITER)
                ?.toIntOrNull()
        )
    }

    override suspend fun getLocationById(id: Int): Result<LocationDetail> = tryCall {
        locationService.getLocationById(id).toDomain()
    }

    override suspend fun getEpisodesByIds(ids: List<Int>): Result<List<EpisodeDetail>> = tryCall {
        if (ids.size == 1) {
            listOf(episodeService.getEpisodeById(ids.first()).toDomain())
        } else {
            episodeService.getEpisodesByIds(
                ids.joinToString(COMMA_DELIMITER)
            ).map { it.toDomain() }
        }
    }
}
