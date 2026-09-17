package com.adrc95.rickyandmorty.presentation.builder

import com.adrc95.rickyandmorty.presentation.filter.model.FilterGroupDisplayModel
import com.adrc95.rickyandmorty.presentation.filter.model.FilterOptionDisplayModel
import org.jetbrains.compose.resources.StringResource
import com.adrc95.rickyandmorty.generated.resources.Res
import com.adrc95.rickyandmorty.generated.resources.species

class FilterGroupDisplayModelBuilder {
    var id: String = "species"
    var title: StringResource = Res.string.species
    var options: List<FilterOptionDisplayModel> = listOf(
        FilterOptionDisplayModel.Species.Human,
        FilterOptionDisplayModel.Species.Alien
    )

    fun withId(id: String) = apply { this.id = id }
    fun withTitle(title: StringResource) = apply { this.title = title }
    fun withOptions(options: List<FilterOptionDisplayModel>) = apply { this.options = options }

    fun build() = FilterGroupDisplayModel(
        id = id,
        title = title,
        options = options
    )
}

fun filterGroupDisplayModel(block: FilterGroupDisplayModelBuilder.() -> Unit = {}): FilterGroupDisplayModel =
    FilterGroupDisplayModelBuilder().apply(block).build()
