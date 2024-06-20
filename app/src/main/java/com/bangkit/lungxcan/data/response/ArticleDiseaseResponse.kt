package com.bangkit.lungxcan.data.response

import com.google.gson.annotations.SerializedName

data class ArticleDiseaseResponse(

    @field:SerializedName("ArticleDiseaseResponse")
    val articleDiseaseResponse: List<ArticleDiseaseResponseItem>
)

data class ArticleDiseaseResponseItem(

    @field:SerializedName("summary")
    val summary: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("url")
    val url: String
)
