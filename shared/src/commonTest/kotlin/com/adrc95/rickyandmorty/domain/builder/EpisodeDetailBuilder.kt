package com.adrc95.rickyandmorty.domain.builder

import com.adrc95.rickyandmorty.domain.model.EpisodeDetail

class EpisodeDetailBuilder {
    var id: Int = 1
    var name: String = "Pilot"
    var episode: String = "S01E01"
    var airDate: String = "December 2, 2013"

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withEpisode(episode: String) = apply { this.episode = episode }
    fun withAirDate(airDate: String) = apply { this.airDate = airDate }

    fun build() = EpisodeDetail(
        id = id,
        name = name,
        episode = episode,
        airDate = airDate
    )
}

fun episodeDetail(block: EpisodeDetailBuilder.() -> Unit = {}): EpisodeDetail =
    EpisodeDetailBuilder().apply(block).build()
