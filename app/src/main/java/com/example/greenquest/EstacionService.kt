package com.example.greenquest

import com.example.greenquest.apiParameters.LoginResponse
import com.example.greenquest.apiParameters.Request
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EstacionService {

    @POST(Constants.PATH_API + "login/")
    suspend fun login(@Body body: Request): Response<AuthSuccessResponse>

    @POST(Constants.PATH_API + "signup/")
    suspend fun signup(@Body body: Request): Response<AuthSuccessResponse>

    @POST(Constants.PATH_API + "residuos/reclamar/")
    suspend fun residuoReclamar(@Body body: ReclamarResiduoRequest): Response<ReclamarResiduoGenericResponse>


    @POST(Constants.PATH_API + "logout/")
    suspend fun logout(@Body token: String): Response<Unit>
}
