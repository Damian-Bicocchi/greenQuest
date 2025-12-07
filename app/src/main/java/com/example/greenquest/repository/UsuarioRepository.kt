package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.apiParameters.LoginResponse
import com.example.greenquest.apiParameters.SingupResponse
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.HttpException
import retrofit2.Response

class UsuarioRepository {

    private val api = RetrofitInstance.api

    suspend fun signup(username: String, password: String): Response<AuthSuccessResponse> {
        return api.signup(Request(username, password))
    }

    suspend fun login(username: String, password: String): Response<AuthSuccessResponse> {
        return api.login(Request(username, password))
    }
}

