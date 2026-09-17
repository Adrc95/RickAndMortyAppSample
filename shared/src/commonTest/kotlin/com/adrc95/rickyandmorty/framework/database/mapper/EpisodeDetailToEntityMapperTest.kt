package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class EpisodeDetailToEntityMapperTest {

    @Test
    fun given_episode_detail_when_mapped_to_entity_then_returns_episode_detail_entity() {
        // Given
        val domain = episodeDetail {
            withId(1)
            withName("Pilot")
            withEpisode("S01E01")
            withAirDate("December 2, 2013")
        }
        val expected = episodeDetailEntity {
            withId(1)
            withCharacterId(10)
            withName("Pilot")
            withEpisode("S01E01")
            withAirDate("December 2, 2013")
        }

        // When
        val result = domain.toEntity(characterId = 10)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_episode_detail_when_mapped_to_entity_then_character_id_is_set_correctly() {
        // Given
        val domain = episodeDetail {
            withId(25)
        }

        // When
        val result = domain.toEntity(characterId = 42)

        // Then
        assertEquals(42, result.characterId)
        assertEquals(25, result.id)
    }
}
