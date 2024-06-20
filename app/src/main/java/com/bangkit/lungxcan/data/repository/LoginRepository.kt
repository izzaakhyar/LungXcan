package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.liveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.auth.AuthApiService
import com.bangkit.lungxcan.data.pref.UserModel
import com.bangkit.lungxcan.data.pref.UserPreference
import com.bangkit.lungxcan.data.request.LoginRequest
import com.bangkit.lungxcan.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class LoginRepository private constructor(
    private val apiService: AuthApiService,
    private val userPreference: UserPreference
) {
    fun signIn(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val loginRequest = LoginRequest(email, password)
            val successResponse = apiService.login(loginRequest)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null
        fun getInstance(apiService: AuthApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService, userPreference)
            }.also { instance = it }
    }
}