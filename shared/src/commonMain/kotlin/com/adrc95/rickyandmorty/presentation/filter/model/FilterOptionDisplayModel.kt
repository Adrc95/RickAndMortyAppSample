package com.adrc95.rickyandmorty.presentation.filter.model

import org.jetbrains.compose.resources.StringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.filter_gender_female
import com.adrc95.rickyandmorty.generated.resources.filter_gender_genderless
import com.adrc95.rickyandmorty.generated.resources.filter_gender_male
import com.adrc95.rickyandmorty.generated.resources.filter_gender_unknown
import com.adrc95.rickyandmorty.generated.resources.filter_species_alien
import com.adrc95.rickyandmorty.generated.resources.filter_species_animal
import com.adrc95.rickyandmorty.generated.resources.filter_species_cronenberg
import com.adrc95.rickyandmorty.generated.resources.filter_species_disease
import com.adrc95.rickyandmorty.generated.resources.filter_species_human
import com.adrc95.rickyandmorty.generated.resources.filter_species_mythological_creature
import com.adrc95.rickyandmorty.generated.resources.filter_species_poopybutthole
import com.adrc95.rickyandmorty.generated.resources.filter_species_robot
import com.adrc95.rickyandmorty.generated.resources.filter_species_unknown
import com.adrc95.rickyandmorty.generated.resources.filter_status_alive
import com.adrc95.rickyandmorty.generated.resources.filter_status_dead
import com.adrc95.rickyandmorty.generated.resources.filter_status_unknown
import com.adrc95.rickyandmorty.generated.resources.genders
import com.adrc95.rickyandmorty.generated.resources.species
import com.adrc95.rickyandmorty.generated.resources.status

sealed class FilterOptionDisplayModel(
    open val label: StringResource
) {

    sealed class Species(
        override val label: StringResource
    ) : FilterOptionDisplayModel(Res.string.species) {

        data object Human : Species(Res.string.filter_species_human)

        data object Alien : Species(Res.string.filter_species_alien)

        data object Robot : Species(Res.string.filter_species_robot)

        data object Animal : Species(Res.string.filter_species_animal)

        data object Disease : Species(Res.string.filter_species_disease)

        data object Cronenberg : Species(Res.string.filter_species_cronenberg)

        data object Poopybutthole : Species(Res.string.filter_species_poopybutthole)

        data object MythologicalCreature : Species(Res.string.filter_species_mythological_creature)

        data object Unknown : Species(Res.string.filter_species_unknown)
    }

    sealed class Gender(
        override val label: StringResource
    ) : FilterOptionDisplayModel(Res.string.genders) {

        data object Female : Gender(Res.string.filter_gender_female)

        data object Male : Gender(Res.string.filter_gender_male)

        data object Genderless : Gender(Res.string.filter_gender_genderless)

        data object Unknown : Gender(Res.string.filter_gender_unknown)
    }

    sealed class Status(
        override val label: StringResource
    ) : FilterOptionDisplayModel(Res.string.status) {

        data object Alive : Status(Res.string.filter_status_alive)

        data object Dead : Status(Res.string.filter_status_dead)

        data object Unknown : Status(Res.string.filter_status_unknown)
    }
}
