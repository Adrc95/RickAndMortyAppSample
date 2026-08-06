package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.model.FilterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import javax.inject.Inject

class GetFilterGroupsUseCase @Inject constructor() {

    operator fun invoke(): List<FilterGroup> = listOf(
        FilterGroup(
            id = SPECIES_GROUP_ID,
            options = listOf(
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
        ),
        FilterGroup(
            id = GENDER_GROUP_ID,
            options = listOf(
                FilterOption(id = "female"),
                FilterOption(id = "male"),
                FilterOption(id = "genderless"),
                FilterOption(id = "unknown"),
            ),
        ),
        FilterGroup(
            id = STATUS_GROUP_ID,
            options = listOf(
                FilterOption(id = "alive"),
                FilterOption(id = "dead"),
                FilterOption(id = "unknown"),
            ),
        ),
    )

    companion object {
        const val SPECIES_GROUP_ID = "species"
        const val GENDER_GROUP_ID = "gender"
        const val STATUS_GROUP_ID = "status"
    }
}
