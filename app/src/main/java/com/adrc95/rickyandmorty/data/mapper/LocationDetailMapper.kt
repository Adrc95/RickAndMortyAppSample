package com.adrc95.rickyandmorty.data.mapper

import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.framework.database.entity.LocationDetailEntity

fun LocationDetail.toEntity(characterId: Int, isOrigin: Boolean): LocationDetailEntity = LocationDetailEntity(
    id = id,
    characterId = characterId,
    name = name,
    type = type,
    dimension = dimension,
    residents = residentsCount,
    isOrigin = isOrigin
)
