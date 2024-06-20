package com.bangkit.lungxcan.data.api.auth

import com.bangkit.lungxcan.data.request.LoginRequest
import com.bangkit.lungxcan.data.request.RegisterRequest
import com.bangkit.lungxcan.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    )
}