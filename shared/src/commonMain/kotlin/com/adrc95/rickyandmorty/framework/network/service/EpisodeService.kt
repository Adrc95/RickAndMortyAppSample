package com.adrc95.rickyandmorty.framework.network.service

import com.adrc95.rickyandmorty.framework.network.dto.EpisodeDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface EpisodeService {
    @GET("episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int): EpisodeDto

    @GET("episode/{ids}")
    suspend fun getEpisodesByIds(@Path("ids") ids: String): List<EpisodeDto>
}
