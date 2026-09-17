package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.framework.database.entity.LocationDetailEntity

fun LocationDetailEntity.toDomain(): LocationDetail = LocationDetail(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residentsCount = residents
)
