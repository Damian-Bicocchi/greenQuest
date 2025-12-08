package com.example.greenquest.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.Request

class InicioSesionModel: ViewModel() {
    private val api = RetrofitInstance.api

    fun  iniciarSesion(mail: String,password: String ) = liveData {

        if (!ChequeosUsuario.camposCompletos(mail,password)) {
            emit("Rellene todos los campos")
            return@liveData
        }

        try{

            val response = api.login(Request(mail,password))
            if(response.isSuccessful){
                emit("OK")
            }else{
                emit("Credenciales incorrectas")
            }

        }catch (e: Exception){
            emit("Error de conexion")
        }

    }

}