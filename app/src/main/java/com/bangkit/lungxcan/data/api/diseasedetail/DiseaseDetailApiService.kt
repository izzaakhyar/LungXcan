package com.bangkit.lungxcan.data.api.diseasedetail

import com.bangkit.lungxcan.data.response.DiseaseDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DiseaseDetailApiService {

    @GET("diseases/{id_name}")
    suspend fun getDiseaseDetail(
        @Path("id_name") idName: String
    ): DiseaseDetailResponse
}