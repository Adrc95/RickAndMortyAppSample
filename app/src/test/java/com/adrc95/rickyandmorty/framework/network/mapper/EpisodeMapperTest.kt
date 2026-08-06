package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.network.builder.episodeDto
import org.junit.Assert.assertEquals
import org.junit.Test

class EpisodeMapperTest {

    @Test
    fun `given episode dto when mapped to domain then returns episode detail`() {
        // Given
        val dto = episodeDto {
            withId(1)
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
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `given episode dto with different data when mapped to domain then returns correct values`() {
        // Given
        val dto = episodeDto {
            withId(25)
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
        val result = dto.toDomain()

        // Then
        assertEquals(expected, result)
    }
}
