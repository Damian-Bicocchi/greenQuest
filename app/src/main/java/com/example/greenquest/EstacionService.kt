package com.example.greenquest

import com.example.greenquest.apiParameters.LoginResponse
import com.example.greenquest.apiParameters.Request
import com.example.greenquest.apiParameters.SingupResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface EstacionService {

    @POST(Constants.PATH_API+"login/")
    suspend fun login(@Body body: Request): LoginResponse

    @POST(Constants.PATH_API+"signup/")
    suspend fun signup(@Body body: Request): SingupResponse

}