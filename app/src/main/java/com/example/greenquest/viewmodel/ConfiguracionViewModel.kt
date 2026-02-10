package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greenquest.Provider.LogroProvider
import com.example.greenquest.repository.TiendaAdquiridosRepository
import com.example.greenquest.repository.UsuarioRepository

class ConfiguracionViewModel: ViewModel() {

    suspend fun cerrarSesion(): Result<Unit> {
        val usuarioLocal = UsuarioRepository.obtenerUsuarioLocal()
        if (usuarioLocal != null) {
            UsuarioRepository.desactivarSesionUsuarioLocal(usuarioLocal)
        }
        LogroProvider.cerrarSesionLogros()
        TiendaAdquiridosRepository.limpiarSesion()
        return UsuarioRepository.logout()
    }
}