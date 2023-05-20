package com.example.myapplication.api

import com.example.myapplication.models.UserRequest
import com.example.myapplication.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("users/signUp")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("users/signIn")
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>
}