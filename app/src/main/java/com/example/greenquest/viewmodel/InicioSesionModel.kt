package com.example.greenquest.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.Request

class InicioSesionModel: ViewModel() {
    private val api = RetrofitInstance.api

    fun  iniciarSesion(mail: String,password: String ) = liveData {

        if (mail.isEmpty() || password.isEmpty()) {
            emit("Rellene todos los campos")
            return@liveData
        }

        if (api.login(Request(mail, password)).access != null) {
            emit("OK")
        } else {
            emit("Usuario o contraseña incorrectos")
        }

    }

}