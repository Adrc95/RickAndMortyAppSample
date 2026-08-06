package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.framework.network.dto.LocationDto

fun LocationDto.toDomain(): LocationDetail = LocationDetail(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    residentsCount = residents.size
)
