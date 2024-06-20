package com.bangkit.lungxcan.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("user")
    val user: User
)

data class User(

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("id_user")
    val idUser: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String
)
