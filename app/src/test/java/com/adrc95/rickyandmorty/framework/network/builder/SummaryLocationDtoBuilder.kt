package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.SummaryLocationDto

class SummaryLocationDtoBuilder {
    var name: String = "Earth (C-137)"
    var url: String = "https://rickandmortyapi.com/api/location/1"

    fun withName(name: String) = apply { this.name = name }
    fun withUrl(url: String) = apply { this.url = url }

    fun build() = SummaryLocationDto(
        name = name,
        url = url,
    )
}

fun summaryLocationDto(block: SummaryLocationDtoBuilder.() -> Unit = {}): SummaryLocationDto =
    SummaryLocationDtoBuilder().apply(block).build()
