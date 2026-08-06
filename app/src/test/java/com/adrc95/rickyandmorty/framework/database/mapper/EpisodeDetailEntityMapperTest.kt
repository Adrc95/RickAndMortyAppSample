package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.database.builder.episodeDetailEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeDetailEntityMapperTest {

    @Test
    fun `given episode detail entity when mapped to domain then returns episode detail`() {
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
    fun `given episode detail entity with different data when mapped to domain then returns correct values`() {
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
