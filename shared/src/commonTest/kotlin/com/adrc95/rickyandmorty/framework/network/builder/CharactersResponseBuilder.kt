package com.adrc95.rickyandmorty.framework.network.builder

import com.adrc95.rickyandmorty.framework.network.dto.CharacterDto
import com.adrc95.rickyandmorty.framework.network.dto.CharactersResponse
import com.adrc95.rickyandmorty.framework.network.dto.PaginationInfoDto

class CharactersResponseBuilder {
    var info: PaginationInfoDto = paginationInfoDto()
    var results: List<CharacterDto> = listOf(characterDto())

    fun withInfo(info: PaginationInfoDto) = apply { this.info = info }
    fun withResults(results: List<CharacterDto>) = apply { this.results = results }

    fun build() = CharactersResponse(
        info = info,
        results = results
    )
}

fun charactersResponse(block: CharactersResponseBuilder.() -> Unit = {}): CharactersResponse =
    CharactersResponseBuilder().apply(block).build()
