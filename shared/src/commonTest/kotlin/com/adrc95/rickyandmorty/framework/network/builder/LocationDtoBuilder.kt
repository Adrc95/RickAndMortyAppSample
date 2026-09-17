package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.LocationDto

class LocationDtoBuilder {
    var id: Int = 1
    var name: String = "Earth (C-137)"
    var type: String = "Planet"
    var dimension: String = "Dimension C-137"
    var residents: List<String> = listOf(
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2"
    )
    var url: String = "https://rickandmortyapi.com/api/location/1"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withType(type: String) = apply { this.type = type }
    fun withDimension(dimension: String) = apply { this.dimension = dimension }
    fun withResidents(residents: List<String>) = apply { this.residents = residents }
    fun withUrl(url: String) = apply { this.url = url }

    fun build() = LocationDto(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents,
        url = url
    )
}

fun locationDto(block: LocationDtoBuilder.() -> Unit = {}): LocationDto = LocationDtoBuilder().apply(block).build()
