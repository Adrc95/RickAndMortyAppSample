package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class EpisodeDetailEntityMapperTest {

    @Test
    fun given_episode_detail_entity_when_mapped_to_domain_then_returns_episode_detail() {
        // Given
        val entity = episodeDetailEntity {
            withId(1)
            withCharacterId(1)
            withName("Pilot")
            withEpisode("S01E01")
            withAirDate("December 2, 2013")
        }
        val expected = episodeDetail {
            withId(1)
            withName("Pilot")
            withEpisode("S01E01")
            withAirDate("December 2, 2013")
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_episode_detail_entity_with_different_data_when_mapped_to_domain_then_returns_correct_values() {
        // Given
        val entity = episodeDetailEntity {
            withId(25)
            withCharacterId(1)
            withName("The Wedding Squanchers")
            withEpisode("S02E10")
            withAirDate("October 4, 2015")
        }
        val expected = episodeDetail {
            withId(25)
            withName("The Wedding Squanchers")
            withEpisode("S02E10")
            withAirDate("October 4, 2015")
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expected, result)
    }
}
