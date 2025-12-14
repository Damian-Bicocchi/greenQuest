package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.Response

import com.example.greenquest.User
import com.example.greenquest.apiParameters.TipoResiduo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UsuarioRepository {
    private val api = RetrofitInstance.api
    private val userDao by lazy {
        GreenQuestApp.instance.database.userDao()
    }

    suspend fun signup(username: String, password: String): Response<AuthSuccessResponse> {
        return api.signup(Request(username, password))
    }

    suspend fun login(username: String, password: String): Response<AuthSuccessResponse> {
        return api.login(Request(username, password))
    }

    suspend fun ranking(tipoResiduo: TipoResiduo?): List<User> {
        val ranking = api.ranking(tipoResiduo);
        val users: List<User> = ranking.map { rank ->
            User(
                Int.MIN_VALUE,
                rank.username,
                null,
                null,
                null,
                rank.total_puntos,
                null
            )
        }
        return users
    }

    suspend fun rankingPosition(tipoResiduo: TipoResiduo?): Int {
        val rank = obtenerUsuarioLocal()?.let { api.rankingPosition(it.uid) }
        return rank?.posicion ?: Int.MIN_VALUE
    }

    suspend fun score(userId: Int? = null, tipoResiduo: TipoResiduo? = null): Int {
        if (tipoResiduo == null) {
            api.puntos(userId)
        } else {
            api.
        }
    }

    suspend fun obtenerUsuarioLocal(): User? = withContext(Dispatchers.IO) {
        userDao.getFirstUser()
    }

    suspend fun guardarUsuarioLocal(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(user)
    }

    suspend fun eliminarUsuarioLocal(user: User) = withContext(Dispatchers.IO) {
        userDao.delete(user)
    }
}

