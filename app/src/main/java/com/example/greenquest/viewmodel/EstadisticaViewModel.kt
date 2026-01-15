package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.repository.EstadisticasRepository
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class EstadisticaViewModel : ViewModel() {

    private val _residuos = MutableStateFlow<List<HistorialResiduo>>(emptyList())
    val residuos : StateFlow<List<HistorialResiduo>> = _residuos.asStateFlow()

    private val _residuosEntreFechas = MutableStateFlow<Map<TipoResiduo, Int>>(emptyMap())
    val residuosEntreFechas: StateFlow<Map<TipoResiduo, Int>> = _residuosEntreFechas.asStateFlow()

    private val _puntosEntreFechas = MutableStateFlow<Map<String, Int>>(emptyMap())
    val puntosEntreFechas: StateFlow<Map<String, Int>> = _puntosEntreFechas.asStateFlow()


    fun obtenerResiduos(){
        viewModelScope.launch {
            try {
                val lista = EstadisticasRepository.obtenerTodosLosResiduos(UsuarioRepository.obtenerIdUsuarioActual())
                _residuos.value = lista
            }
            catch (e: Exception){
                Log.e("estadisticaLogging", "hubo un error en obtenerResiduos ${e.message}")
            }
        }

    }

    fun obtenerResiduosEntreFechas(fechaInicio: Long?, fechaFin: Long?){
        viewModelScope.launch {
            try{
                val mapeo = EstadisticasRepository.obtenerResiduosEntre(
                    fechaInicio,
                    fechaFin,
                    UsuarioRepository.obtenerIdUsuarioActual() )
                _residuosEntreFechas.value = HashMap(mapeo)
            } catch (e: Exception){
                Log.e("estadisticaLogging", "hubo un error en obtenerResiduosEntreFechas $e")
            }
        }
    }

    fun obtenerPuntosEntreFechas(fechaInicio: Long?, fechaFin: Long?){
        viewModelScope.launch {
            try {
                val mapeo = EstadisticasRepository.obtenerPuntosEntre(
                    fechaInicio,
                    fechaFin,
                    UsuarioRepository.obtenerIdUsuarioActual()
                )
                _puntosEntreFechas.value = HashMap(mapeo)

            } catch(e: Exception){
                Log.e("estadisticasLogging", "Hubo un error en obtenerPuntosEntreFechas $e")
            }
        }
    }
}