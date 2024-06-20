package com.bangkit.lungxcan.data.api.map

import com.bangkit.lungxcan.data.response.MapResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApiService {
    @GET("nearbysearch/json")
    suspend fun getNearbyHospital(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String
    ): MapResponse
}