package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.EpisodeDto

class EpisodeDtoBuilder {
    var id: Int = 1
    var name: String = "Pilot"
    var episode: String = "S01E01"
    var airDate: String = "December 2, 2013"
    var characters: List<String> = listOf(
        "https://rickandmortyapi.com/api/character/1",
        "https://rickandmortyapi.com/api/character/2"
    )
    var url: String = "https://rickandmortyapi.com/api/episode/1"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withEpisode(episode: String) = apply { this.episode = episode }
    fun withAirDate(airDate: String) = apply { this.airDate = airDate }
    fun withCharacters(characters: List<String>) = apply { this.characters = characters }
    fun withUrl(url: String) = apply { this.url = url }

    fun build() = EpisodeDto(
        id = id,
        name = name,
        episode = episode,
        airDate = airDate,
        characters = characters,
        url = url,
    )
}

fun episodeDto(block: EpisodeDtoBuilder.() -> Unit = {}): EpisodeDto =
    EpisodeDtoBuilder().apply(block).build()
