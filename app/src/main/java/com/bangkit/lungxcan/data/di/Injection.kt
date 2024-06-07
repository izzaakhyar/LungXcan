package com.bangkit.lungxcan.data.di

import com.bangkit.lungxcan.data.repository.ArticleRepository

object Injection {
    fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository.getInstance()
    }
}