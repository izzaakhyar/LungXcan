package com.bangkit.lungxcan.data.api

import com.bangkit.lungxcan.data.LoginRequest
import com.bangkit.lungxcan.data.RegisterRequest
import com.bangkit.lungxcan.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
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