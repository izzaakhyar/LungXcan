package com.bangkit.lungxcan.data.api.article

import com.bangkit.lungxcan.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiService {
    @GET("everything")
    fun getLungHealthArticle(
        @Query("q") q: String = "Lung health",
        @Query("sortBy") sortBy: String = "publishedAt"
    ): Call<ArticleResponse>
}