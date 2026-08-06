package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.model.Favorite
import com.adrc95.rickyandmorty.framework.network.dto.FavoriteDto

fun FavoriteDto.toDomain(): Favorite = Favorite(
    color = color,
    food = food,
    random = random,
    song = song
)
