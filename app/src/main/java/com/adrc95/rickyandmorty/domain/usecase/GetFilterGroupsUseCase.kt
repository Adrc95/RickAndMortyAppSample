package com.adrc95.rickyandmorty.domain.usecase

import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_FEMALE
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GENDERLESS
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.GENDER_MALE
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ALIEN
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ANIMAL
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_CRONENBERG
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_DISEASE
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_HUMAN
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_MYTHOLOGICAL
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_POOPYBUTTHOLE
import com.adrc95.rickyandmorty.domain.FilterConstants.SPECIES_ROBOT
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_ALIVE
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_DEAD
import com.adrc95.rickyandmorty.domain.FilterConstants.STATUS_GROUP_ID
import com.adrc95.rickyandmorty.domain.FilterConstants.UNKNOWN
import com.adrc95.rickyandmorty.domain.model.FilterGroup
import com.adrc95.rickyandmorty.domain.model.FilterOption
import org.koin.core.annotation.Factory

@Factory
class GetFilterGroupsUseCase {

    operator fun invoke(): List<FilterGroup> = listOf(
        FilterGroup(
            id = SPECIES_GROUP_ID,
            options = listOf(
                FilterOption(id = SPECIES_HUMAN),
                FilterOption(id = SPECIES_ALIEN),
                FilterOption(id = SPECIES_ROBOT),
                FilterOption(id = SPECIES_ANIMAL),
                FilterOption(id = SPECIES_DISEASE),
                FilterOption(id = SPECIES_CRONENBERG),
                FilterOption(id = SPECIES_POOPYBUTTHOLE),
                FilterOption(id = SPECIES_MYTHOLOGICAL),
                FilterOption(id = UNKNOWN)
            )
        ),
        FilterGroup(
            id = GENDER_GROUP_ID,
            options = listOf(
                FilterOption(id = GENDER_FEMALE),
                FilterOption(id = GENDER_MALE),
                FilterOption(id = GENDER_GENDERLESS),
                FilterOption(id = UNKNOWN)
            )
        ),
        FilterGroup(
            id = STATUS_GROUP_ID,
            options = listOf(
                FilterOption(id = STATUS_ALIVE),
                FilterOption(id = STATUS_DEAD),
                FilterOption(id = UNKNOWN)
            )
        )
    )
}
