package com.adrc95.rickyandmorty.framework.database.mapper

import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.framework.database.builder.locationDetailEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationDetailEntityMapperTest {

    @Test
    fun `given location detail entity when mapped to domain then returns location detail`() {
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
    fun `given location detail entity with zero residents when mapped to domain then returns zero`() {
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
