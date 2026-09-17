package com.adrc95.rickyandmorty.framework.network.service

import com.adrc95.rickyandmorty.framework.network.dto.LocationDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface LocationService {
    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationDto
}
