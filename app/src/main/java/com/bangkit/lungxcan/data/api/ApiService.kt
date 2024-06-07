package com.bangkit.lungxcan.data.api

import com.bangkit.lungxcan.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    fun getLungHealthArticle(
        @Query("q") q: String = "Lung health",
        @Query("sortBy") sortBy: String = "publishedAt"
    ): Call<ArticleResponse>
}