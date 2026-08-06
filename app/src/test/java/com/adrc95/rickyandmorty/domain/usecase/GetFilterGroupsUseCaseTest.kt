package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.FilterOption
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFilterGroupsUseCaseTest {

    private val useCase = GetFilterGroupsUseCase()

    @Test
    fun `when invoke then returns three groups`() {
        // When
        val result = useCase()

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `when invoke then first group is species`() {
        // When
        val result = useCase()

        // Then
        val species = result[0]
        assertEquals(GetFilterGroupsUseCase.SPECIES_GROUP_ID, species.id)
        assertEquals(
            listOf(
                FilterOption(id = "human"),
                FilterOption(id = "alien"),
                FilterOption(id = "robot"),
                FilterOption(id = "animal"),
                FilterOption(id = "disease"),
                FilterOption(id = "cronenberg"),
                FilterOption(id = "poopybutthole"),
                FilterOption(id = "mythological"),
                FilterOption(id = "unknown"),
            ),
            species.options
        )
    }

    @Test
    fun `when invoke then second group is gender`() {
        // When
        val result = useCase()

        // Then
        val gender = result[1]
        assertEquals(GetFilterGroupsUseCase.GENDER_GROUP_ID, gender.id)
        assertEquals(
            listOf(
                FilterOption(id = "female"),
                FilterOption(id = "male"),
                FilterOption(id = "genderless"),
                FilterOption(id = "unknown"),
            ),
            gender.options
        )
    }

    @Test
    fun `when invoke then third group is status`() {
        // When
        val result = useCase()

        // Then
        val status = result[2]
        assertEquals(GetFilterGroupsUseCase.STATUS_GROUP_ID, status.id)
        assertEquals(
            listOf(
                FilterOption(id = "alive"),
                FilterOption(id = "dead"),
                FilterOption(id = "unknown"),
            ),
            status.options
        )
    }

    @Test
    fun `when invoke then species has nine options`() {
        // When
        val result = useCase()

        // Then
        assertEquals(9, result[0].options.size)
    }

    @Test
    fun `when invoke then gender has four options`() {
        // When
        val result = useCase()

        // Then
        assertEquals(4, result[1].options.size)
    }

    @Test
    fun `when invoke then status has three options`() {
        // When
        val result = useCase()

        // Then
        assertEquals(3, result[2].options.size)
    }
}
