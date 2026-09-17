package com.adrc95.rickyandmorty.framework.network.mapper

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.framework.network.builder.episodeDto
import kotlin.test.Test
import kotlin.test.assertEquals

class EpisodeMapperTest {

    @Test
    fun given_episode_dto_when_mapped_to_domain_then_returns_episode_detail() {
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
    fun given_episode_dto_with_different_data_when_mapped_to_domain_then_returns_correct_values() {
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
