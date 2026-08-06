package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.LocationDetail

class LocationDetailBuilder {
    var id: Int = 1
    var name: String = "Earth (C-137)"
    var type: String = "Planet"
    var dimension: String = "Dimension C-137"
    var residentsCount: Int = 27

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withType(type: String) = apply { this.type = type }
    fun withDimension(dimension: String) = apply { this.dimension = dimension }
    fun withResidentsCount(residentsCount: Int) = apply { this.residentsCount = residentsCount }

    fun build() = LocationDetail(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residentsCount = residentsCount
    )
}

fun locationDetail(block: LocationDetailBuilder.() -> Unit = {}): LocationDetail =
    LocationDetailBuilder().apply(block).build()
