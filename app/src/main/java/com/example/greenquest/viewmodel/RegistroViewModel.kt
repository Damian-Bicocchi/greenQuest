package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.UserChecks

import com.example.greenquest.apiParameters.ApiError
import com.example.greenquest.repository.UsuarioRepository
import com.google.gson.Gson


class RegistroViewModel : ViewModel() {

    fun registrar(username: String, password: String, confirm: String) = liveData {

        if (!UserChecks.isFieldsFilled(username, password, confirm)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        if (!UserChecks.isValidUsername(username)) {
            emit("El nombre de usuario solo puede contener letras, números, '.', '-' y '_")
            return@liveData
        }
        if(!UserChecks.isValidUsernameCharacterLength(username)){
            emit("El nombre de usuario debe tener entre 3 y 20 caracteres")
            return@liveData
        }
        if (!UserChecks.isValidPassword(password)) {
            emit("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
            return@liveData
        }
        if (!UserChecks.isValidPasswordConfirmation(password, confirm)) {
            emit("Las contraseñas no coinciden")
            return@liveData
        }

        try {

            val response = UsuarioRepository.signup(username, password)

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
            Log.e("greenQuest", e.message.toString())
            emit("Error de conexión con el servidor")
        }
    }
}
