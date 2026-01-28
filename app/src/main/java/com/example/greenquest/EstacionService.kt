package com.example.greenquest

import com.example.greenquest.apiParameters.Estacion
import com.example.greenquest.apiParameters.LogoutRequest
import com.example.greenquest.apiParameters.PosicionRanking
import com.example.greenquest.apiParameters.PuntosUsuario
import com.example.greenquest.apiParameters.RankingEntry
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.apiParameters.UserInfoResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.example.greenquest.apiParameters.estadisticas.ResiduoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface EstacionService {
    @POST("logout/")
    suspend fun logout(@Body token: LogoutRequest): Response<Unit>

    @POST("residuo/reclamar/")
    suspend fun residuoReclamar(@Body body: ReclamarResiduoRequest): Response<ReclamarResiduoGenericResponse>

    @GET("datos_usuario/")
    suspend fun getUserData(): UserInfoResponse

    @GET("puntos/")
    suspend fun score(@Query("id_user") id: Int? = null): PuntosUsuario

    @GET("ranking/semanal/")
    suspend fun rankingWeekly(@Query("tipo_residuo") tipoResiduo: TipoResiduo? = null): List<RankingEntry>


    @GET("ranking/")
    suspend fun rankingHistorical(@Query("tipo_residuo") tipoResiduo: TipoResiduo? = null): List<RankingEntry>

    @GET("ranking/posicion/")
    suspend fun rankingPosition(
        @Query("id_usuario") id: Int, @Query("tipo_residuo") tipoResiduo: TipoResiduo? = null
    ): PosicionRanking

    @GET("residuos/{id_usuario}/")
    suspend fun cantidadResiduos(
        @Path("id_usuario") idUsuario: Int
    ): List<List<ResiduoItem>>

    @GET("estaciones/")
    suspend fun stations(): List<Estacion>

    @GET("estaciones/{id_estacion}")
    suspend fun station(@Path("id_estacion") idStation: Integer): Estacion
}
