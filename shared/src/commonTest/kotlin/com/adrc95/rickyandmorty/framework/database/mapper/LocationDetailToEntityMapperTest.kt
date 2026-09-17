package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationDetailToEntityMapperTest {

    @Test
    fun given_location_detail_when_mapped_to_entity_as_origin_then_returns_location_detail_entity() {
        // Given
        val domain = locationDetail {
            withId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidentsCount(27)
        }
        val expected = locationDetailEntity {
            withId(1)
            withCharacterId(10)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidents(27)
            withIsOrigin(true)
        }

        // When
        val result = domain.toEntity(characterId = 10, isOrigin = true)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_location_detail_when_mapped_to_entity_as_location_then_is_origin_is_false() {
        // Given
        val domain = locationDetail {
            withId(3)
        }

        // When
        val result = domain.toEntity(characterId = 10, isOrigin = false)

        // Then
        assertEquals(false, result.isOrigin)
        assertEquals(10, result.characterId)
        assertEquals(3, result.id)
    }

    @Test
    fun given_location_detail_when_mapped_to_entity_then_residents_count_maps_to_residents() {
        // Given
        val domain = locationDetail {
            withResidentsCount(42)
        }

        // When
        val result = domain.toEntity(characterId = 1, isOrigin = true)

        // Then
        assertEquals(42, result.residents)
    }
}
