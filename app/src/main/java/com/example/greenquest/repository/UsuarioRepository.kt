package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.Response
import com.example.greenquest.database.user.User
import com.example.greenquest.apiParameters.LogoutRequest
import com.example.greenquest.apiParameters.RankingEntry
import com.example.greenquest.apiParameters.RefreshRequest
import com.example.greenquest.apiParameters.RefreshResponse
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.apiParameters.UserInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

object UsuarioRepository {
    private val api = RetrofitInstance.api
    private val authApi = RetrofitInstance.authApi
    private val userDao by lazy {
        GreenQuestApp.instance.database.userDao()
    }

    suspend fun signup(username: String, password: String): Response<AuthSuccessResponse> {
        return authApi.signup(Request(username, password))
    }

    suspend fun login(username: String, password: String): Response<AuthSuccessResponse> {
        return authApi.login(Request(username, password))
    }

    suspend fun logout(): Result<Unit> {
        val refresh = TokenDataStoreProvider.get().getRefreshToken()
            ?: return Result.failure(Exception("No refresh token"))
        return try {
            api.logout(LogoutRequest(refresh))
            TokenDataStoreProvider.get().clearAllTokens()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(): UserInfoResponse {
        return api.getUserData()
    }

    suspend fun rankingWeekly(tipoResiduo: TipoResiduo? = null): List<RankingEntry> {
        // Intenté que fuera List<User> pero como la API no me devuelve el uid correspondiente y el
        // uid del UserDAO no es automático, no puedo crear una lista de usuarios. Se queda como
        // RankingEntry.
        return try {
            api.rankingWeekly(tipoResiduo)
        } catch (_: Exception) {
            listOf() // TODO: ¿Esto debería manejar el error?
        }
    }

    suspend fun rankingPosition(tipoResiduo: TipoResiduo? = null): Int {
        // TODO: ALERTA! La api solo devuelve la posición del ranking total, no del semanal!
        val rank = obtenerUsuarioLocal()?.let { api.rankingPosition(it.uid) }
        return rank?.posicion ?: Int.MIN_VALUE
    }

    @Deprecated("No se usa en ningún momento en greenQuest")
    suspend fun score(tipoResiduo: TipoResiduo? = null): Int {
        return api.score().puntos
    }

    suspend fun obtenerUsuarioLocal(): User? =
        withContext(Dispatchers.IO) {
            userDao.getFirstUser()
        }

    suspend fun guardarUsuarioLocal(user: User) =
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }

    suspend fun eliminarUsuarioLocal(user: User) =
        withContext(Dispatchers.IO) {
            userDao.delete(user)
    }

    suspend fun getPuntaje(): Int =
        withContext(Dispatchers.IO){
            userDao.getFirstUser()!!.puntos
    }

    suspend fun addPuntaje(addPuntos : Int){
        withContext(Dispatchers.IO){

        }
    }
}

