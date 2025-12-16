package com.example.greenquest

import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.apiParameters.RefreshResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login/")
    suspend fun login(@Body body: Request): Response<AuthSuccessResponse>

    @POST("signup/")
    suspend fun signup(@Body body: Request): Response<AuthSuccessResponse>

    @POST("token/refresh/")
    suspend fun refreshToken(@Body body: RefreshRequest): Response<RefreshResponse>
}
