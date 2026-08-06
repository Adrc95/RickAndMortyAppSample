package com.adrc95.rickyandmorty.data.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeDetailToEntityMapperTest {

    @Test
    fun `given episode detail when mapped to entity then returns episode detail entity`() {
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
    fun `given episode detail when mapped to entity then character id is set correctly`() {
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
