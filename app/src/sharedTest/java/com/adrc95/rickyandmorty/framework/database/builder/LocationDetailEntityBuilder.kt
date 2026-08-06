package com.adrc95.rickyandmorty.framework.database.builder

import com.adrc95.rickyandmorty.framework.database.entity.LocationDetailEntity

class LocationDetailEntityBuilder {
    var id: Int = 1
    var characterId: Int = 1
    var name: String = "Earth (C-137)"
    var type: String = "Planet"
    var dimension: String = "Dimension C-137"
    var residents: Int = 27
    var isOrigin: Boolean = true

    fun withId(id: Int) = apply { this.id = id }
    fun withCharacterId(characterId: Int) = apply { this.characterId = characterId }
    fun withName(name: String) = apply { this.name = name }
    fun withType(type: String) = apply { this.type = type }
    fun withDimension(dimension: String) = apply { this.dimension = dimension }
    fun withResidents(residents: Int) = apply { this.residents = residents }
    fun withIsOrigin(isOrigin: Boolean) = apply { this.isOrigin = isOrigin }

    fun build() = LocationDetailEntity(
        id = id,
        characterId = characterId,
        name = name,
        type = type,
        dimension = dimension,
        residents = residents,
        isOrigin = isOrigin,
    )
}

fun locationDetailEntity(block: LocationDetailEntityBuilder.() -> Unit = {}): LocationDetailEntity =
    LocationDetailEntityBuilder().apply(block).build()
