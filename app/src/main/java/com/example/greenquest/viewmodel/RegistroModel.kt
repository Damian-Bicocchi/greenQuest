package com.example.greenquest.viewmodel

import ChequeosUsuario
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.apiParameters.ApiError
import com.example.greenquest.repository.UsuarioRepository
import com.google.gson.Gson


class RegistroModel : ViewModel() {

    fun registrar(email: String, contraseña: String, confirm: String) = liveData {

        if (!ChequeosUsuario.camposCompletos(email, contraseña, confirm)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        if (!ChequeosUsuario.esValidoCorreo(email)) {
            emit("Email inválido, debe contener '@' y un dominio")
            return@liveData
        }

        if (!ChequeosUsuario.esValidoFormatoContraseña(contraseña)) {
            emit("Contraseña inválida, debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
            return@liveData
        }
        if (!ChequeosUsuario.esValidoConfirmarContraseña(contraseña, confirm)) {
            emit("Contraseña inválida, debe coincidir en ambos campos y tener al menos 8 caracteres")
            return@liveData
        }

        try {

            val response = UsuarioRepository.signup(email, contraseña)

            if (response.isSuccessful) {
                emit("OK")

            } else {

                val errorJson = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                when {
                    apiError.username != null -> emit("Ya existe un usuario con el correo ingresado")

                    apiError.password != null -> emit(apiError.password.first())

                    apiError.non_field_errors != null -> emit(apiError.non_field_errors.first())

                    else -> emit("Error desconocido del servidor")
                }
            }

        } catch (e: Exception) {
            Log.e("RegistroViewModel", e.message.toString())
            emit("Error de conexión con el servidor")
        }
    }
}
