package com.example.greenquest

import com.example.greenquest.apiParameters.Request
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.PosicionRanking
import com.example.greenquest.apiParameters.PuntosUsuario
import com.example.greenquest.apiParameters.RankingEntry
import com.example.greenquest.apiParameters.TipoResiduo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EstacionService {

    @POST("/login/")
    suspend fun login(@Body body: Request): Response<AuthSuccessResponse>

    @POST("/signup/")
    suspend fun signup(@Body body: Request): Response<AuthSuccessResponse>

    @POST("/logout/")
    suspend fun logout(@Body token: String): Response<Unit>

    @GET("/puntos/")
    suspend fun puntos(@Query("id_user") id: Int? = null): PuntosUsuario

    @GET("/ranking/")
    suspend fun ranking(@Query("tipo_residuo") tipoResiduo: TipoResiduo? = null): List<RankingEntry>

    @GET("/ranking/posicion/")
    suspend fun rankingPosition(
        @Path("id_usuario") id: Int, @Query("tipo_residuo") tipoResiduo: TipoResiduo? = null
    ): PosicionRanking
}
