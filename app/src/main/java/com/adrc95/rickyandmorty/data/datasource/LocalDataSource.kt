package com.adrc95.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getCharacterById(id: Int): CharacterEntity?

    fun getCharacters(): PagingSource<Int, CharacterEntity>

    suspend fun getRemoteKey(resource: String): RemoteKeyEntity?

    suspend fun hasCachedCharacters(): Boolean

    suspend fun insertCharacters(characters: List<Character>, nextPage: Int?, deleteOld: Boolean)

    suspend fun saveCharacters(characters: List<Character>)

    suspend fun getLocationByCharacterId(characterId: Int, isOrigin: Boolean): LocationDetail?

    suspend fun saveLocationDetail(location: LocationDetail, characterId: Int, isOrigin: Boolean)

    suspend fun getEpisodesByCharacterId(characterId: Int): List<EpisodeDetail>

    suspend fun saveEpisodeDetails(episodes: List<EpisodeDetail>, characterId: Int)

    suspend fun deleteCharacterDetails(characterId: Int)

    fun isFavourite(characterId: Int): Flow<Boolean>

    suspend fun toggleFavourite(characterId: Int)
}
