package com.example.greenquest

import com.example.greenquest.apiParameters.Request
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.example.greenquest.apiParameters.LogoutRequest
import com.example.greenquest.apiParameters.PosicionRanking
import com.example.greenquest.apiParameters.PuntosUsuario
import com.example.greenquest.apiParameters.RankingEntry
import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.apiParameters.RefreshResponse
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.apiParameters.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EstacionService {

    @POST("login/")
    suspend fun login(@Body body: Request): Response<AuthSuccessResponse>

    /*
    To do: Cambiar el Response por el tipo de dato normal
     */
    @POST("signup/")
    suspend fun signup(@Body body: Request): Response<AuthSuccessResponse>

    @POST("residuo/reclamar/")
    suspend fun residuoReclamar(@Body body: ReclamarResiduoRequest): Response<ReclamarResiduoGenericResponse>


    @POST("logout/")
    suspend fun logout(@Body token: LogoutRequest): Response<Unit>

    @GET("datos_usuario/")
    suspend fun getUserData(): UserInfoResponse

    @POST("token/refresh/")
    suspend fun refreshToken(@Body body: RefreshRequest): RefreshResponse

    @GET("puntos/")
    suspend fun score(@Query("id_user") id: Int? = null): PuntosUsuario

    @GET("ranking/semanal/")
    suspend fun rankingWeekly(@Query("tipo_residuo") tipoResiduo: TipoResiduo? = null): List<RankingEntry>

    @GET("api/ranking/posicion/")
    suspend fun rankingPosition(
        @Query("id_user") id: Int, @Query("tipo_residuo") tipoResiduo: TipoResiduo? = null
    ): PosicionRanking
}
