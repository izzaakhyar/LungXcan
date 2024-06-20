package com.bangkit.lungxcan.data.api.articledisease

import com.bangkit.lungxcan.data.response.ArticleDiseaseResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ArticleDiseaseApiService {

    @GET("news/{disease}")
    suspend fun getDiseaseDetail(
        @Path("disease") disease: String
    ): List<ArticleDiseaseResponseItem>
}