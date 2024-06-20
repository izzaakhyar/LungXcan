package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.articledisease.ArticleDiseaseApiService
import com.bangkit.lungxcan.data.response.ArticleDiseaseResponse
import com.bangkit.lungxcan.data.response.ArticleDiseaseResponseItem
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class ArticleDiseaseRepository(private val apiService: ArticleDiseaseApiService) {

    fun getArticleDiseaseDetail(disease: String): LiveData<ResultState<List<ArticleDiseaseResponseItem>>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getDiseaseDetail(disease)
                emit(ResultState.Success(response))
            } catch (e: SocketTimeoutException) {
                emit(ResultState.Error("Request timed out. Please try again."))
            } catch (e: IOException) {
                emit(ResultState.Error("Network error. Please check your internet connection."))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ArticleDiseaseResponse::class.java)
                emit(ResultState.Error(e.message ?: "An unknown error occurred"))
            }
        }

    companion object {
        @Volatile
        private var instance: ArticleDiseaseRepository? = null
        fun getInstance(apiService: ArticleDiseaseApiService) =
            instance ?: synchronized(this) {
                instance ?: ArticleDiseaseRepository(apiService)
            }.also { instance = it }
    }
}