package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("dimension")
    val dimension: String,
    @SerialName("residents")
    val residents: List<String>,
    @SerialName("url")
    val url: String
)
