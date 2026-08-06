package com.adrc95.rickyandmorty.framework.network.service

import com.adrc95.rickyandmorty.framework.network.dto.EpisodeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface EpisodeService {
    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeDto

    @GET("episode/{ids}")
    suspend fun getEpisodesByIds(@Path("ids") ids: String): List<EpisodeDto>
}
