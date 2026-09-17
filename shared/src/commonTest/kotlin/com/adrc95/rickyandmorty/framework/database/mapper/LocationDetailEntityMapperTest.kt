package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import kotlin.test.Test
import kotlin.test.assertEquals

class LocationDetailEntityMapperTest {

    @Test
    fun given_location_detail_entity_when_mapped_to_domain_then_returns_location_detail() {
        // Given
        val entity = locationDetailEntity {
            withId(1)
            withCharacterId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidents(27)
            withIsOrigin(true)
        }
        val expected = locationDetail {
            withId(1)
            withName("Earth (C-137)")
            withType("Planet")
            withDimension("Dimension C-137")
            withResidentsCount(27)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun given_location_detail_entity_with_zero_residents_when_mapped_to_domain_then_returns_zero() {
        // Given
        val entity = locationDetailEntity {
            withResidents(0)
        }

        // When
        val result = entity.toDomain()

        // Then
        assertEquals(0, result.residentsCount)
    }
}
