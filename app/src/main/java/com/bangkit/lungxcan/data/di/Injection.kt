package com.bangkit.lungxcan.data.di

import android.content.Context
import com.bangkit.lungxcan.data.api.articledisease.ArticleDiseaseApiConfig
import com.bangkit.lungxcan.data.api.auth.AuthApiConfig
import com.bangkit.lungxcan.data.api.diseasedetail.DiseaseDetailApiConfig
import com.bangkit.lungxcan.data.api.map.MapApiConfig
import com.bangkit.lungxcan.data.api.user.UserApiConfig
import com.bangkit.lungxcan.data.pref.UserPreference
import com.bangkit.lungxcan.data.pref.dataStore
import com.bangkit.lungxcan.data.repository.ArticleDiseaseRepository
import com.bangkit.lungxcan.data.repository.ArticleRepository
import com.bangkit.lungxcan.data.repository.DiseaseRepository
import com.bangkit.lungxcan.data.repository.LoginRepository
import com.bangkit.lungxcan.data.repository.MapRepository
import com.bangkit.lungxcan.data.repository.RegisterRepository
import com.bangkit.lungxcan.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideArticleRepository(): ArticleRepository {
        return ArticleRepository.getInstance()
    }

    fun provideMapRepository(): MapRepository {
        val mapApiService = MapApiConfig.getMapApi()
        return MapRepository.getInstance(mapApiService)
    }

    fun provideLoginRepository(context: Context): LoginRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = AuthApiConfig.getAuthApiService()
        return LoginRepository.getInstance(apiService, pref)
    }

    fun provideRegisterRepository(context: Context): RegisterRepository {
        val apiService = AuthApiConfig.getAuthApiService()
        return RegisterRepository.getInstance(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = UserApiConfig.getApiService(user.token)
        return UserRepository.getInstance()
    }

    fun provideDiseaseRepository(): DiseaseRepository {
        val apiService = DiseaseDetailApiConfig.getDiseaseDetailApiService()
        return DiseaseRepository.getInstance(apiService)
    }

    fun provideArticleDiseaseRepository(): ArticleDiseaseRepository {
        val apiService = ArticleDiseaseApiConfig.getArticleDiseaseApiService()
        return ArticleDiseaseRepository.getInstance(apiService)
    }
}