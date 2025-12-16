package com.example.greenquest.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.launch

class MiPerfilViewModel: ViewModel() {

    fun cerrarSesion() {
        viewModelScope.launch{
            val usuarioLocal = UsuarioRepository.obtenerUsuarioLocal()
            if (usuarioLocal != null) {
                UsuarioRepository.eliminarUsuarioLocal(usuarioLocal)
            }
            UsuarioRepository.logout()

        }
    }
}