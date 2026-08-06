package com.adrc95.rickyandmorty.presentation.builder

import com.adrc95.rickyandmorty.domain.builder.episodeDetail
import com.adrc95.rickyandmorty.domain.builder.locationDetail
import com.adrc95.rickyandmorty.domain.builder.summaryLocation
import com.adrc95.rickyandmorty.domain.model.EpisodeDetail
import com.adrc95.rickyandmorty.domain.model.LocationDetail
import com.adrc95.rickyandmorty.domain.model.SummaryLocation
import com.adrc95.rickyandmorty.presentation.core.model.CharacterDisplayModel
import com.adrc95.rickyandmorty.presentation.core.model.CharacterStatusDisplayModel

class CharacterDisplayModelBuilder {
    var id: Int = 1
    var name: String = "Rick Sanchez"
    var status: CharacterStatusDisplayModel = CharacterStatusDisplayModel.ALIVE
    var species: String = "Human"
    var type: String = ""
    var gender: String = "Male"
    var origin: SummaryLocation = summaryLocation()
    var originDetail: LocationDetail? = locationDetail()
    var location: SummaryLocation = summaryLocation()
    var locationDetail: LocationDetail? = locationDetail()
    var image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    var episodeIds: List<Int> = listOf(1, 2)
    var episodeDetails: List<EpisodeDetail> = listOf(episodeDetail())
    var isFavourite: Boolean = false

    fun withId(id: Int) = apply { this.id = id }
    fun withName(name: String) = apply { this.name = name }
    fun withStatus(status: CharacterStatusDisplayModel) = apply { this.status = status }
    fun withSpecies(species: String) = apply { this.species = species }
    fun withType(type: String) = apply { this.type = type }
    fun withGender(gender: String) = apply { this.gender = gender }
    fun withOrigin(origin: SummaryLocation) = apply { this.origin = origin }
    fun withOriginDetail(originDetail: LocationDetail?) = apply { this.originDetail = originDetail }
    fun withLocation(location: SummaryLocation) = apply { this.location = location }
    fun withLocationDetail(locationDetail: LocationDetail?) = apply { this.locationDetail = locationDetail }
    fun withImage(image: String) = apply { this.image = image }
    fun withEpisodeIds(episodeIds: List<Int>) = apply { this.episodeIds = episodeIds }
    fun withEpisodeDetails(episodeDetails: List<EpisodeDetail>) = apply { this.episodeDetails = episodeDetails }
    fun withIsFavourite(isFavourite: Boolean) = apply { this.isFavourite = isFavourite }

    fun build() = CharacterDisplayModel(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin,
        originDetail = originDetail,
        location = location,
        locationDetail = locationDetail,
        image = image,
        episodeIds = episodeIds,
        episodeDetails = episodeDetails,
        isFavourite = isFavourite,
    )
}

fun characterDisplayModel(block: CharacterDisplayModelBuilder.() -> Unit = {}): CharacterDisplayModel =
    CharacterDisplayModelBuilder().apply(block).build()
