package com.example.greenquest.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.User
import com.example.greenquest.apiParameters.Request
import com.example.greenquest.repository.UsuarioRepository

class InicioSesionModel: ViewModel() {
    private val api = RetrofitInstance.api

    fun  iniciarSesion(userName: String,password: String ) = liveData {

        if (!ChequeosUsuario.camposCompletos(userName,password)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        try{

            val response = api.login(Request(userName,password))
            if(response.isSuccessful){
                emit("OK")
                UsuarioRepository.guardarUsuarioLocal(
                    User(
                        uid = 0,
                        userName = userName,
                        password = password,
                        accessToken = response.body()!!.access,
                        refreshToken = response.body()!!.refresh
                    )
                )
            }else{
                emit("Credenciales incorrectas")
            }

        }catch (e: Exception){
            emit("Error de conexion")
        }

    }

}