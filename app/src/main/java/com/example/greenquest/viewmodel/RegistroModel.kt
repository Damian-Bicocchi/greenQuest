package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.ChequeosUsuario
import com.example.greenquest.repository.UsuarioRepository
import retrofit2.HttpException

class RegistroViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    fun registrar(email: String, password: String, confirm: String) = liveData {

        if (!ChequeosUsuario.esValidoCorreo(email)) {
            emit("Email inválido, debe contener '@' y un dominio")
            return@liveData
        }

        if (!ChequeosUsuario.esValidoConfirmarContraseña(password, confirm)) {
            emit("Contraseña inválida, debe coincidir en ambos campos y tener al menos 8 caracteres")
            return@liveData
        }

        // Intentamos registrar directamente en el servidor
        val response = repository.signup(email, password)
        // Si el servidor devuelve objeto de signup exitoso (ajustar según implementación del backend)
        Log.println(Log.ASSERT, "RegistroViewModel", "Signup response: $response")
        if(response.access== null){
            emit("Error en el registro, el usuario ya existe o datos inválidos")
            return@liveData
        }
        emit("OK")


    }
}