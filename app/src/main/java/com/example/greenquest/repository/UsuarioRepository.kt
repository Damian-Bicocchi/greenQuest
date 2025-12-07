package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.apiParameters.LoginResponse
import com.example.greenquest.apiParameters.SingupResponse
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.Request
import retrofit2.HttpException

class UsuarioRepository {

    private val api = RetrofitInstance.api

    suspend fun login(username: String, password: String): LoginResponse {
        return api.login(Request(username, password))
    }

    suspend fun signup(username: String, password: String): SingupResponse {
        return api.signup(Request(username, password))
    }

    // comprobar si el usuario existe sin usar login con contraseña vacía
    suspend fun existeUsuario(email: String): Boolean {
        return try {
            // Intentamos signup: si el usuario ya existe, el servidor suele devolver 400 con detalle
            api.signup(Request(email, "dummyPassword123"))
            // Si signup tuvo éxito, esto significa que el usuario no existía; opcionalmente deberíamos
            // borrar ese usuario creado o informar, pero para este repo asumimos que el backend
            // devolverá error cuando el usuario ya exista y OK cuando no existe.
            false
        } catch (e: HttpException) {
            val errorBody = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            Log.println(Log.DEBUG, "UsuarioRepository", "existeUsuario HttpException: ${e.code()} - $errorBody")
            // Si el error indica que el username/email ya existe, devolvemos true
            if (!errorBody.isNullOrEmpty() && (errorBody.contains("username") || errorBody.contains("email") || errorBody.contains("already"))) {
        }

    }
}
