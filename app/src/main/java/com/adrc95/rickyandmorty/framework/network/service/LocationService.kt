package com.adrc95.rickyandmorty.framework.network.service

import com.adrc95.rickyandmorty.framework.network.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationService {
    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: Int): LocationDto
}
