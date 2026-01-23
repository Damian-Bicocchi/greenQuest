package com.example.greenquest.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.estadisticas.PeriodoResiduo
import com.example.greenquest.repository.EstadisticasRepository
import com.example.greenquest.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstadisticaViewModel : ViewModel() {

    private val _residuos = MutableStateFlow<List<HistorialResiduo>>(emptyList())
    val residuos : StateFlow<List<HistorialResiduo>> = _residuos.asStateFlow()

    private val _residuosEntreFechas = MutableStateFlow<Map<TipoResiduo, Int>>(emptyMap())
    val residuosEntreFechas: StateFlow<Map<TipoResiduo, Int>> = _residuosEntreFechas.asStateFlow()

    private val _puntosEntreFechas = MutableStateFlow(0)
    val puntosEntreFechas: StateFlow<Int> = _puntosEntreFechas.asStateFlow()


    fun obtenerResiduos(){
        viewModelScope.launch {
            try {
                val idUsuario = UsuarioRepository.obtenerIdUsuarioActual()
                val lista = EstadisticasRepository.obtenerTodosLosResiduos(idUsuario = idUsuario)
                _residuos.value = lista

                Log.e("reporteLogging", "la lista es $lista y el idDe usuario es $idUsuario")
            }
            catch (e: Exception){
                Log.e("estadisticaLogging", "hubo un error en obtenerResiduos ${e.message}")
            }
        }

    }


    fun obtenerResiduosPorPeriodo(periodoResiduo: PeriodoResiduo){
        viewModelScope.launch {
            try {
                val idUsuario = UsuarioRepository.obtenerIdUsuarioActual()
                val mapeo = EstadisticasRepository.obtenerResiduosEnRangoFecha(periodoResiduo, idUsuario)
                _residuosEntreFechas.value = mapeo
            } catch (e: Exception) {
                Log.e("estadisticaLogging", "hubo un error en obtenerResiduosEntreFechas $e")
            }
        }
    }

    fun obtenerPuntosPorPeriodo(periodoResiduo: PeriodoResiduo){

        viewModelScope.launch(Dispatchers.IO){
            viewModelScope.launch {
                try {
                    val idUsuario = UsuarioRepository.obtenerIdUsuarioActual()
                    val cantidadTotal = EstadisticasRepository.obtenerPuntajeEnRangoFecha(periodoResiduo, idUsuario)
                    _puntosEntreFechas.value = cantidadTotal
                } catch (e: Exception) {
                    // Manejar error
                    _puntosEntreFechas.value = 0
                }
            }
        }
    }
}