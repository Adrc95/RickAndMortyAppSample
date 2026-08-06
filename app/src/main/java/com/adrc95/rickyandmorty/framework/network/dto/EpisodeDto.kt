package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("episode")
    val episode: String,
    @SerialName("air_date")
    val airDate: String,
    @SerialName("characters")
    val characters: List<String>,
    @SerialName("url")
    val url: String,
)
