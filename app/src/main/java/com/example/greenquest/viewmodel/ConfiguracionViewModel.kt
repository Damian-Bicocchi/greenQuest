package com.example.greenquest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.greenquest.repository.UsuarioRepository

class ConfiguracionViewModel: ViewModel() {

    suspend fun cerrarSesion(): Result<Unit> {
        val usuarioLocal = UsuarioRepository.obtenerUsuarioLocal()
        if (usuarioLocal != null) {
            UsuarioRepository.eliminarUsuarioLocal(usuarioLocal)
        }
        return UsuarioRepository.logout()
    }
}