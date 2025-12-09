package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.AuthSuccessResponse
import com.example.greenquest.apiParameters.Request
import retrofit2.HttpException
import retrofit2.Response
import com.example.greenquest.database.AppDatabase

import com.example.greenquest.User
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


    suspend fun obtenerUsuarioLocal(): User? =
        withContext(Dispatchers.IO) {
            userDao.getFirtUser()
        }

    suspend fun guardarUsuarioLocal(user: User) =
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }

    suspend fun eliminarUsuarioLocal(user: User) =
        withContext(Dispatchers.IO) {
            userDao.delete(user)
    }
}

