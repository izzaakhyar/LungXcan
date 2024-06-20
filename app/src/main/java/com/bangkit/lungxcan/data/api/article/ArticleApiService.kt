package com.bangkit.lungxcan.data.api.article

import com.bangkit.lungxcan.data.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApiService {
    @GET("everything")
    fun getLungHealthArticle(
        @Query("q") q: String = "cancer OR pneumonia OR tbc OR covid OR fibrosis OR pleural",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("searchIn") searchIn: String = "title",
    ): Call<ArticleResponse>
}