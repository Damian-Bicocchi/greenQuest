package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.ErrorHandlerProvider
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.Response
import com.example.greenquest.database.user.User
import com.example.greenquest.apiParameters.LogoutRequest
import com.example.greenquest.apiParameters.PosicionRanking
import com.example.greenquest.apiParameters.RankingEntry
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
    private val errorHandler = GreenQuestApp.instance.errorHandler

    private var usuario: User? = null
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
        return try {
            api.getUserData()
        } catch (e: HttpException) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            UserInfoResponse()
        }
    }

    suspend fun rankingWeekly(tipoResiduo: TipoResiduo? = null): List<RankingEntry> {
        // Intenté que fuera List<User> pero como la API no me devuelve el uid correspondiente y el
        // uid del UserDAO no es automático, no puedo crear una lista de usuarios. Se queda como
        // RankingEntry.
        return try {
            api.rankingWeekly(tipoResiduo)
        } catch (e: HttpException) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            emptyList()
        }
    }

    suspend fun rankingHistorical(tipoResiduo: TipoResiduo? = null): List<RankingEntry> {
        return try {
            api.rankingHistorical(tipoResiduo)
        } catch (e: Exception) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            emptyList()
        }
    }

    suspend fun rankingPosition(tipoResiduo: TipoResiduo? = null): Int {
        // La api solo devuelve la posición del ranking total, no del semanal!
        val rank = obtenerUsuarioLocal()?.let { try {
                api.rankingPosition(it.uid, tipoResiduo)
            } catch (e: Exception) {
                errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
                PosicionRanking(Int.MIN_VALUE)
            }
        }
        return rank?.posicion ?: Int.MIN_VALUE
    }

    suspend fun score(idUser: Int? = null): Int {
        return try {
            api.score(idUser).puntos
        } catch (e: Exception) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            Int.MIN_VALUE
        }
    }

    suspend fun obtenerUsuarioLocal(): User?{
        if (usuario == null) {
            return withContext(Dispatchers.IO) {
                userDao.getFirstUser()
            }
        }else{
            return usuario
        }
    }


    suspend fun guardarUsuarioLocal(user: User){
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
        usuario = user
    }


    suspend fun eliminarUsuarioLocal(user: User){
        usuario = null
        withContext(Dispatchers.IO) {
            userDao.delete(user)
        }
    }


    suspend fun cantReciduosUsuario(id : Int){
        try{
            val recursosReciclados = api.cantReciduosUsuario(id)
            val usuario = obtenerUsuarioLocal()
            if (usuario != null) {
                for (item in recursosReciclados) {
                    Log.d("UsuarioRepository", "Tipo residuo: ${item.nombre}, Cantidad: ${item.cantidad}, id usuario: $id")
                    usuario.incrementarCantidadResiduo(item.nombre, item.cantidad)
                }
                actualizarUsuarioLocal(usuario)
            }
        }
        catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al obtener residuos del usuario", e)
        }
    }

    suspend fun incrementarCantidadResiduoLocal(tipoResiduo: TipoResiduo){
        val usuario = obtenerUsuarioLocal()
        if (usuario != null) {
            usuario.incrementarCantidadResiduo(tipoResiduo)
            actualizarUsuarioLocal(usuario)
        }
    }

    suspend fun actualizarUsuarioLocal(user: User) =
        withContext(Dispatchers.IO) {
            userDao.updateUser(user)
        }

    suspend fun obtenerIdUsuarioActual(): Int {
        val id = withContext(Dispatchers.IO){
            userDao.getFirstUser()?.uid ?: -1
        }
        return id
    }

}

