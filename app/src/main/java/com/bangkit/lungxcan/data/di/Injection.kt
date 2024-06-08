package com.bangkit.lungxcan.data.di

import com.bangkit.lungxcan.data.api.MapApiConfig
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.repository.MapRepository

object Injection {
    fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository.getInstance()
    }

    fun provideMapRepository(): MapRepository {
        val mapApiService = MapApiConfig.getMapApi()
        return MapRepository.getInstance(mapApiService)
    }
}