package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

import com.example.greenquest.apiParameters.ApiError
import com.example.greenquest.repository.UsuarioRepository
import com.google.gson.Gson

class RegistroViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    fun registrar(email: String, password: String, confirm: String) = liveData {

        // 1️⃣ Validaciones locales
        if (!ChequeosUsuario.esValidoCorreo(email)) {
            emit("Email inválido, debe contener '@' y un dominio")
            return@liveData
        }

        if (!ChequeosUsuario.esValidoConfirmarContraseña(password, confirm)) {
            emit("Contraseña inválida, debe coincidir en ambos campos y tener al menos 8 caracteres")
            return@liveData
        }

        try {
            // 2️⃣ Llamada al backend
            val response = repository.signup(email, password)

            if (response.isSuccessful) {

                emit("Registro exitoso ✅")
            } else {

                val errorJson = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                when {
                    apiError.username != null ->
                        emit(apiError.username.first())

                    apiError.password != null ->
                        emit(apiError.password.first())

                    apiError.non_field_errors != null ->
                        emit(apiError.non_field_errors.first())

                    else ->
                        emit("Error desconocido del servidor")
                }
            }

        } catch (e: Exception) {
            Log.e("RegistroViewModel", e.message.toString())
            emit("Error de conexión con el servidor")
        }
    }
}
