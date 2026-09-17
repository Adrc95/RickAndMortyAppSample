package com.adrc95.rickyandmorty.presentation.core.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CharacterToDisplayModelMapperTest {

    @Test
    fun given_character_when_mapped_to_display_model_then_returns_character_display_model() {
        // Given
        val domain = character {
            withId(1)
            withName("Rick Sanchez")
            withStatus("Alive")
            withSpecies("Human")
            withType("")
            withGender("Male")
            withOrigin(
                summaryLocation {
                    withId(1)
                    withName("Earth (C-137)")
                }
            )
            withLocation(
                summaryLocation {
                    withId(3)
                    withName("Citadel of Ricks")
                }
            )
            withImage("https://rickandmortyapi.com/api/character/avatar/1.jpeg")
            withEpisodeIds(listOf(1, 2))
            withCreated("2017-11-04T18:48:46.250Z")
            withIsFavourite(true)
        }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(1, result.id)
        assertEquals("Rick Sanchez", result.name)
        assertEquals(CharacterStatusDisplayModel.ALIVE, result.status)
        assertEquals("Human", result.species)
        assertEquals("", result.type)
        assertEquals("Male", result.gender)
        assertEquals("Earth (C-137)", result.origin.name)
        assertEquals("Citadel of Ricks", result.location.name)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", result.image)
        assertEquals(listOf(1, 2), result.episodeIds)
        assertEquals(true, result.isFavourite)
    }

    @Test
    fun given_character_with_details_when_mapped_to_display_model_then_preserves_details() {
        // Given
        val domain = character { withId(1) }
        val originDetail = locationDetail {
            withId(1)
            withName("Earth (C-137)")
        }
        val locDetail = locationDetail {
            withId(3)
            withName("Citadel of Ricks")
        }
        val episodes = listOf(
            episodeDetail {
                withId(1)
                withName("Pilot")
            },
            episodeDetail {
                withId(2)
                withName("Lawnmower Dog")
            }
        )

        // When
        val result = domain.toDisplayModel(
            originDetail = originDetail,
            locationDetail = locDetail,
            episodeDetails = episodes
        )

        // Then
        assertEquals(originDetail, result.originDetail)
        assertEquals(locDetail, result.locationDetail)
        assertEquals(episodes, result.episodeDetails)
    }

    @Test
    fun given_character_when_mapped_to_display_model_without_details_then_details_are_null() {
        // Given
        val domain = character { withId(1) }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertNull(result.originDetail)
        assertNull(result.locationDetail)
        assertEquals(emptyList<Any>(), result.episodeDetails)
    }

    @Test
    fun given_character_status_alive_when_mapped_to_display_model_then_returns_alive_status() {
        // Given
        val domain = character { withStatus("Alive") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.ALIVE, result.status)
    }

    @Test
    fun given_character_status_dead_when_mapped_to_display_model_then_returns_dead_status() {
        // Given
        val domain = character { withStatus("Dead") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.DEAD, result.status)
    }

    @Test
    fun given_character_status_unknown_when_mapped_to_display_model_then_returns_unknown_status() {
        // Given
        val domain = character { withStatus("unknown") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.UNKNOWN, result.status)
    }
}
