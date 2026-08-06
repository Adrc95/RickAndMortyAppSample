package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationInfoDto(
    @SerialName("count")
    val count: Int,
    @SerialName("pages")
    val pages: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("prev")
    val prev: String?
)
