package com.adrc95.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.adrc95.rickyandmorty.data.mapper.toEntity
import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.framework.database.AppDatabase
import com.adrc95.rickyandmorty.framework.database.dao.CharacterDao
import com.adrc95.rickyandmorty.framework.database.dao.EpisodeDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.LocationDetailDao
import com.adrc95.rickyandmorty.framework.database.dao.RemoteKeyDao
import com.adrc95.rickyandmorty.framework.database.entity.CharacterEntity
import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity
import com.adrc95.rickyandmorty.framework.database.mapper.toDomain
import com.adrc95.rickyandmorty.data.mapper.toEntity as locationToEntity
import com.adrc95.rickyandmorty.data.mapper.toEntity as episodeToEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val database: AppDatabase,
    private val characterDao: CharacterDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val locationDetailDao: LocationDetailDao,
    private val episodeDetailDao: EpisodeDetailDao,
) : LocalDataSource {

    override suspend fun getCharacterById(id: Int): CharacterEntity? =
        characterDao.getById(id)

    override fun getCharacters(): PagingSource<Int, CharacterEntity> =
        characterDao.getCharacters()

    override suspend fun getRemoteKey(resource: String): RemoteKeyEntity? =
        remoteKeyDao.get(resource)

    override suspend fun hasCachedCharacters(): Boolean = characterDao.count() > 0

    override suspend fun insertCharacters(
        characters: List<Character>,
        nextPage: Int?,
        deleteOld: Boolean
    ) {
        database.withTransaction {
            if (deleteOld) {
                remoteKeyDao.clear()
                characterDao.clear()
            }
            characterDao.insertAll(characters.map { it.toEntity() })
            remoteKeyDao.insert(
                RemoteKeyEntity(
                    resource = "characters",
                    nextPage = nextPage,
                    lastUpdatedAt = System.currentTimeMillis(),
                )
            )
        }
    }

    override suspend fun saveCharacters(characters: List<Character>) {
        characterDao.insertAll(characters.map { it.toEntity() })
    }

    override suspend fun getLocationByCharacterId(characterId: Int, isOrigin: Boolean): LocationDetail? =
        locationDetailDao.getByCharacterId(characterId, isOrigin)?.toDomain()

    override suspend fun saveLocationDetail(location: LocationDetail, characterId: Int, isOrigin: Boolean) {
        locationDetailDao.insert(location.locationToEntity(characterId, isOrigin))
    }

    override suspend fun getEpisodesByCharacterId(characterId: Int): List<EpisodeDetail> =
        episodeDetailDao.getByCharacterId(characterId).map { it.toDomain() }

    override suspend fun saveEpisodeDetails(episodes: List<EpisodeDetail>, characterId: Int) {
        episodeDetailDao.insertAll(episodes.map { it.episodeToEntity(characterId) })
    }

    override suspend fun deleteCharacterDetails(characterId: Int) {
        database.withTransaction {
            locationDetailDao.deleteByCharacterId(characterId)
            episodeDetailDao.deleteByCharacterId(characterId)
        }
    }

    override fun isFavourite(characterId: Int): Flow<Boolean> =
        characterDao.isFavourite(characterId)

    override suspend fun toggleFavourite(characterId: Int) {
        characterDao.toggleFavourite(characterId)
    }
}
