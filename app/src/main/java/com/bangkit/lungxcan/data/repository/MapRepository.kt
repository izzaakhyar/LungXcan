package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.lungxcan.BuildConfig
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.map.MapApiService
import com.bangkit.lungxcan.data.response.MapResponse

class MapRepository private constructor(private val mapApiService: MapApiService) {

    fun getHospital(location: String): LiveData<ResultState<MapResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = mapApiService.getNearbyHospital(
                location,
                50000,
                "Hospital",
                "Hospital||rumah sakit",
                BuildConfig.MAP_API_KEY
            )
            if (response.status == "OK") {
                emit(ResultState.Success(response))
            } else {
                emit(ResultState.Error("Failed to get nearby hospitals"))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: MapRepository? = null
        fun getInstance(mapApiService: MapApiService) =
            instance ?: synchronized(this) {
                instance ?: MapRepository(mapApiService)
            }.also { instance = it }
    }
}