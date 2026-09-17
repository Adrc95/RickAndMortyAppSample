package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_GROUP_ID
import com.adrc95.rickyandmorty.domain.model.FilterOption
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFilterGroupsUseCaseTest {

    private val useCase = GetFilterGroupsUseCase()

    @Test
    fun when_invoke_then_returns_three_groups() {
        // When
        val result = useCase()

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun when_invoke_then_first_group_is_species() {
        // When
        val result = useCase()

        // Then
        val species = result[0]
        assertEquals(SPECIES_GROUP_ID, species.id)
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
                FilterOption(id = "unknown")
            ),
            species.options
        )
    }

    @Test
    fun when_invoke_then_second_group_is_gender() {
        // When
        val result = useCase()

        // Then
        val gender = result[1]
        assertEquals(GENDER_GROUP_ID, gender.id)
        assertEquals(
            listOf(
                FilterOption(id = "female"),
                FilterOption(id = "male"),
                FilterOption(id = "genderless"),
                FilterOption(id = "unknown")
            ),
            gender.options
        )
    }

    @Test
    fun when_invoke_then_third_group_is_status() {
        // When
        val result = useCase()

        // Then
        val status = result[2]
        assertEquals(STATUS_GROUP_ID, status.id)
        assertEquals(
            listOf(
                FilterOption(id = "alive"),
                FilterOption(id = "dead"),
                FilterOption(id = "unknown")
            ),
            status.options
        )
    }

    @Test
    fun when_invoke_then_species_has_nine_options() {
        // When
        val result = useCase()

        // Then
        assertEquals(9, result[0].options.size)
    }

    @Test
    fun when_invoke_then_gender_has_four_options() {
        // When
        val result = useCase()

        // Then
        assertEquals(4, result[1].options.size)
    }

    @Test
    fun when_invoke_then_status_has_three_options() {
        // When
        val result = useCase()

        // Then
        assertEquals(3, result[2].options.size)
    }
}
