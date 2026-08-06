package com.adrc95.rickyandmorty.framework.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    @SerialName("info")
    val info: PaginationInfoDto,
    @SerialName("results")
    val results: List<CharacterDto>
)