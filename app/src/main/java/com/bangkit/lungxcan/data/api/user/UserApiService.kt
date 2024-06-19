package com.bangkit.lungxcan.data.api.user

import com.bangkit.lungxcan.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface UserApiService {

    @GET("users/profile")
    fun getProfile(): Call<UserResponse>
}