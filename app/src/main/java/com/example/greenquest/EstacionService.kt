package com.example.greenquest

import com.example.greenquest.apiParameters.LoginResponse
import com.example.greenquest.apiParameters.Request
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.example.greenquest.apiParameters.LogoutRequest
import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.apiParameters.RefreshResponse
import com.example.greenquest.apiParameters.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EstacionService {

    @POST(Constants.PATH_API + "login/")
    suspend fun login(@Body body: Request): Response<AuthSuccessResponse>
    /*
    To do: Cambiar el Response por el tipo de dato normal
     */
    @POST(Constants.PATH_API + "signup/")
    suspend fun signup(@Body body: Request): Response<AuthSuccessResponse>

    @POST(Constants.PATH_API + "residuo/reclamar/")
    suspend fun residuoReclamar(@Body body: ReclamarResiduoRequest): ReclamarResiduoGenericResponse


    @POST(Constants.PATH_API + "logout/")
    suspend fun logout(@Body token: LogoutRequest): Response<Unit>

    @GET(Constants.PATH_API + "datos_usuario/")
    suspend fun  getUserData(): UserInfoResponse

    @POST(Constants.PATH_API +"token/refresh/")
    suspend fun refreshToken(@Body body: RefreshRequest): Response<RefreshResponse>
}
