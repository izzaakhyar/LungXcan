package com.bangkit.lungxcan.data.api.diseasedetail

import com.bangkit.lungxcan.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DiseaseDetailApiConfig {

    fun getDiseaseDetailApiService(): DiseaseDetailApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_CC_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(DiseaseDetailApiService::class.java)
    }
}