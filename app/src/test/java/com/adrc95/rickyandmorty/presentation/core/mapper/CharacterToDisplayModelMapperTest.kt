package com.adrc95.rickyandmorty.presentation.core.mapper

import com.adrc95.rickyandmorty.domain.builder.character
import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CharacterToDisplayModelMapperTest {

    @Test
    fun `given character when mapped to display model then returns character display model`() {
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
    fun `given character with details when mapped to display model then preserves details`() {
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
    fun `given character when mapped to display model without details then details are null`() {
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
    fun `given character status alive when mapped to display model then returns alive status`() {
        // Given
        val domain = character { withStatus("Alive") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.ALIVE, result.status)
    }

    @Test
    fun `given character status dead when mapped to display model then returns dead status`() {
        // Given
        val domain = character { withStatus("Dead") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.DEAD, result.status)
    }

    @Test
    fun `given character status unknown when mapped to display model then returns unknown status`() {
        // Given
        val domain = character { withStatus("unknown") }

        // When
        val result = domain.toDisplayModel()

        // Then
        assertEquals(CharacterStatusDisplayModel.UNKNOWN, result.status)
    }
}
