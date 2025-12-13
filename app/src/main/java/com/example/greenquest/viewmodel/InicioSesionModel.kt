package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.User
import com.example.greenquest.repository.UsuarioRepository

class InicioSesionModel: ViewModel() {

    fun iniciarSesion(userName: String,password: String ) = liveData {

        if (!ChequeosUsuario.camposCompletos(userName,password)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        try{
            val response = UsuarioRepository.login(userName,password)

            if(!response.isSuccessful){
                emit("Usuario o contraseña incorrectos")
                return@liveData
            }
            val body = response.body()
            if(body != null) {
                TokenDataStoreProvider.get().saveAccessToken(body.access)
                TokenDataStoreProvider.get().saveRefreshToken(body.refresh)

                val id = UsuarioRepository.getUserProfile()

                UsuarioRepository.guardarUsuarioLocal(
                    User(
                        uid = id.id!!,
                        userName = userName,
                        password = password,
                        puntos = 0,
                        imagen = null
                    )
                )
                emit("OK")
            }else{
                emit("Error desconocido")
            }
        }catch (e: Exception){
            Log.d("InicioSesionModel", "Error de conexion: ${e.printStackTrace()}")
            emit("Error de conexion")
        }

    }

}