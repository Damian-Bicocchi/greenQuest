package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.repository.EstadisticasRepository
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstadisticaViewModel : ViewModel() {

    private val _residuos = MutableStateFlow<List<HistorialResiduo>>(emptyList())
    val residuos : StateFlow<List<HistorialResiduo>> = _residuos.asStateFlow()

    fun obtenerResiduos(){
        viewModelScope.launch {
            try {
                val lista = EstadisticasRepository.obtenerTodosLosResiduos(UsuarioRepository.obtenerIdUsuarioActual())
                _residuos.value = lista
            }
            catch (e: Exception){
                Log.e("estadisticaLogging", "hubo un error en 26 ${e.message}")
            }
        }

    }
}