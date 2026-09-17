package com.adrc95.rickyandmorty.framework.database.builder

import com.adrc95.rickyandmorty.framework.database.entity.EpisodeDetailEntity

class EpisodeDetailEntityBuilder {
    var id: Int = 1
    var characterId: Int = 1
    var name: String = "Pilot"
    var episode: String = "S01E01"
    var airDate: String = "December 2, 2013"

    fun withId(id: Int) = apply { this.id = id }
    fun withCharacterId(characterId: Int) = apply { this.characterId = characterId }
    fun withName(name: String) = apply { this.name = name }
    fun withEpisode(episode: String) = apply { this.episode = episode }
    fun withAirDate(airDate: String) = apply { this.airDate = airDate }

    fun build() = EpisodeDetailEntity(
        id = id,
        characterId = characterId,
        name = name,
        episode = episode,
        airDate = airDate,
    )
}

fun episodeDetailEntity(block: EpisodeDetailEntityBuilder.() -> Unit = {}): EpisodeDetailEntity =
    EpisodeDetailEntityBuilder().apply(block).build()
