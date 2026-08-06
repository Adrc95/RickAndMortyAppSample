
package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SummaryLocationDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String
)
