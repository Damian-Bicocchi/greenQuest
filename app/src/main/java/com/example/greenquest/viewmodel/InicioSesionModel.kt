package com.example.greenquest.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.ChequeosUsuario
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.database.user.User
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InicioSesionModel: ViewModel() {
    private val _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    fun iniciarSesion(userName: String,password: String ) {

        if (!ChequeosUsuario.camposCompletos(userName,password)) {
            _status.value = "Rellene todos los campos"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = UsuarioRepository.login(userName,password)

                if(!response.isSuccessful){
                    _status.value = "Usuario o contraseÃ±a incorrectos"
                    return@launch
                }
                val body = response.body()
                if (body == null) {
                    _status.value = "Error desconocido"
                    return@launch
                }

                TokenDataStoreProvider.get().saveAccessToken(body.access)
                TokenDataStoreProvider.get().saveRefreshToken(body.refresh)

                val id = UsuarioRepository.getUserProfile()
                UsuarioRepository.guardarUsuarioLocal(
                    User(
                        uid = id.id!!,
                        userName = userName,
                        password = password,
                        puntos = 0,
                        imagen = null,
                        descripcion = null
                    )
                )
                UsuarioRepository.cantReciduosUsuario(id.id)
                _status.value = "OK"
            }catch (e: Exception){
                Log.d("InicioSesionModel", "Error de conexion", e)
                _status.value = "Error de conexion"
            }
        }

    }

}