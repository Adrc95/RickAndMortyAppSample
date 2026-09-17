package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.Character
import com.adrc95.rickyandmorty.domain.model.SummaryLocation

class CharacterBuilder {
    var id: Int = 1
    var name: String = "Rick Sanchez"
    var status: String = "Alive"
    var species: String = "Human"
    var type: String = ""
    var gender: String = "Male"
    var origin: SummaryLocation = summaryLocation()
    var location: SummaryLocation = summaryLocation()
    var image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    var episodeIds: List<Int> = listOf(1, 2)
    var created: String = "2017-11-04T18:48:46.250Z"
    var isFavourite: Boolean = false

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withStatus(status: String) = apply { this.status = status }
    fun withSpecies(species: String) = apply { this.species = species }
    fun withType(type: String) = apply { this.type = type }
    fun withGender(gender: String) = apply { this.gender = gender }
    fun withOrigin(origin: SummaryLocation) = apply { this.origin = origin }
    fun withLocation(location: SummaryLocation) = apply { this.location = location }
    fun withImage(image: String) = apply { this.image = image }
    fun withEpisodeIds(episodeIds: List<Int>) = apply { this.episodeIds = episodeIds }
    fun withCreated(created: String) = apply { this.created = created }
    fun withIsFavourite(isFavourite: Boolean) = apply { this.isFavourite = isFavourite }

    fun build() = Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin,
        location = location,
        image = image,
        episodeIds = episodeIds,
        created = created,
        isFavourite = isFavourite
    )
}

fun character(block: CharacterBuilder.() -> Unit = {}): Character = CharacterBuilder().apply(block).build()
