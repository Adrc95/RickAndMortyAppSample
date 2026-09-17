package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.CharacterDto
import com.adrc95.rickyandmorty.framework.network.dto.SummaryLocationDto

class CharacterDtoBuilder {
    var id: Int = 1
    var name: String = "Rick Sanchez"
    var status: String = "Alive"
    var species: String = "Human"
    var type: String = ""
    var gender: String = "Male"
    var origin: SummaryLocationDto = summaryLocationDto()
    var location: SummaryLocationDto = summaryLocationDto()
    var image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    var episode: List<String> = listOf(
        "https://rickandmortyapi.com/api/episode/1",
        "https://rickandmortyapi.com/api/episode/2"
    )
    var url: String = "https://rickandmortyapi.com/api/character/1"
    var created: String = "2017-11-04T18:48:46.250Z"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withStatus(status: String) = apply { this.status = status }
    fun withSpecies(species: String) = apply { this.species = species }
    fun withType(type: String) = apply { this.type = type }
    fun withGender(gender: String) = apply { this.gender = gender }
    fun withOrigin(origin: SummaryLocationDto) = apply { this.origin = origin }
    fun withLocation(location: SummaryLocationDto) = apply { this.location = location }
    fun withImage(image: String) = apply { this.image = image }
    fun withEpisode(episode: List<String>) = apply { this.episode = episode }
    fun withUrl(url: String) = apply { this.url = url }
    fun withCreated(created: String) = apply { this.created = created }

    fun build() = CharacterDto(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin,
        location = location,
        image = image,
        episode = episode,
        url = url,
        created = created
    )
}

fun characterDto(block: CharacterDtoBuilder.() -> Unit = {}): CharacterDto = CharacterDtoBuilder().apply(block).build()
