package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.model.RemoteKey
import com.adrc95.rickyandmorty.framework.database.entity.RemoteKeyEntity

fun RemoteKeyEntity.toDomain(): RemoteKey = RemoteKey(
    nextPage = nextPage,
    lastUpdatedAt = lastUpdatedAt
)
