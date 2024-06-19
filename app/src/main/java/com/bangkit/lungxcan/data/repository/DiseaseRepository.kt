package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.liveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.diseasedetail.DiseaseDetailApiService
import com.bangkit.lungxcan.data.response.DiseaseDetailResponse
import com.google.gson.Gson
import retrofit2.HttpException

class DiseaseRepository(private val apiService: DiseaseDetailApiService) {

    fun getDiseaseDetail(idName: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getDiseaseDetail(idName)
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DiseaseDetailResponse::class.java)
            emit(ResultState.Error(e.message()))
        }
    }

    companion object {
        @Volatile
        private var instance: DiseaseRepository? = null
        fun getInstance(apiService: DiseaseDetailApiService) =
            instance ?: synchronized(this) {
                instance ?: DiseaseRepository(apiService)
            }.also { instance = it }
    }
}