package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.apiParameters.ApiError
import com.example.greenquest.repository.UsuarioRepository
import com.google.gson.Gson


class RegistroViewModel : ViewModel() {

    fun registrar(username: String, contraseña: String, confirm: String) = liveData {

        if (!ChequeosUsuario.camposCompletos(username, contraseña, confirm)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        if (!ChequeosUsuario.esValidoUsername(username)) {
            emit("username inválido, debe tener entre 3 y 20 caracteres y solo puede contener letras, números, puntos, guiones y guiones bajos")
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

            val response = UsuarioRepository.signup(username, contraseña)

            if (response.isSuccessful) {
                emit("OK")

            } else {

                val errorJson = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorJson, ApiError::class.java)

                when {
                    apiError.username != null ->
                        emit("Ya existe un usuario con el nombre de usuario ingresado")

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
