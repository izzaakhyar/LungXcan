package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.liveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.auth.AuthApiService
import com.bangkit.lungxcan.data.request.RegisterRequest
import com.bangkit.lungxcan.data.response.RegisterResponse
import com.google.gson.Gson
import retrofit2.HttpException

class RegisterRepository private constructor(
    private val apiService: AuthApiService
) {
    fun createAccount(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val registerRequest = RegisterRequest(name, email, password)
            val successResponse = apiService.register(registerRequest)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message.let { ResultState.Error(it) })
        }
    }

    companion object {
        @Volatile
        private var instance: RegisterRepository? = null
        fun getInstance(apiService: AuthApiService) =
            instance ?: synchronized(this) {
                instance ?: RegisterRepository(apiService)
            }.also { instance = it }
    }
}