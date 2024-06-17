package com.bangkit.lungxcan.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.bangkit.lungxcan.data.ResultState
import com.bangkit.lungxcan.data.api.UserApiConfig
import com.bangkit.lungxcan.data.response.User
import com.bangkit.lungxcan.data.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

//    private val _user = MutableLiveData<User>()
//    val user: LiveData<User> = _user

    fun getUserInfo(token: String, user: MutableLiveData<User>) {
        ResultState.Loading
        val client = UserApiConfig.getApiService(token).getProfile()
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                ResultState.Loading
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        user.value = responseBody.user
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {

            }

        })
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: UserRepository()
            }.also { instance = it }
    }
}