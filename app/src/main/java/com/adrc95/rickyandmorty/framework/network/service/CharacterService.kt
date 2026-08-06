package com.adrc95.rickyandmorty.framework.network.service

import com.adrc95.rickyandmorty.framework.network.dto.CharacterDto
import com.adrc95.rickyandmorty.framework.network.dto.CharactersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null,
        @Query("status") status: String? = null
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacterDetailById(@Path("id") id: Int): CharacterDto
}
