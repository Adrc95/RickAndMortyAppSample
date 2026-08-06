package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    val color: String,
    val food: String,
    @SerialName("random_string")
    val random: String,
    val song: String
)
