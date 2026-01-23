package com.example.greenquest.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.repository.ReporteRepository
import com.example.greenquest.states.reporte.EstadoReporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReporteViewModel : ViewModel(){
    private val _reportState = MutableLiveData(EstadoReporte.SIN_REPORTE)
    val reporteState: LiveData<EstadoReporte> = _reportState
    private val _reporteMensajeFallido = MutableLiveData("")
    val reporteMensajeFallido : LiveData<String> = _reporteMensajeFallido

    private val _tipoResiduoSeleccionado = MutableStateFlow<TipoResiduo?>(null)
    val tipoResiduoSeleccionado: StateFlow<TipoResiduo?> = _tipoResiduoSeleccionado



    fun processReport(idResiduo: String, imageData: Bitmap){
        if (idResiduo.isEmpty()) {
            _reportState.value = EstadoReporte.REPORTE_FALLIDO
            _reporteMensajeFallido.value = "Residuo vacío"
            return
        }

        if (_tipoResiduoSeleccionado.equals(null)){
            _reportState.value = EstadoReporte.REPORTE_FALLIDO
            _reporteMensajeFallido.value = "No seleccionó una categoría"
            return
        }


        viewModelScope.launch {
            try {
                ReporteRepository.insertarReporte(
                    imageData = imageData,
                    clasificacionUsuario = _tipoResiduoSeleccionado.value!!,
                    idResiduo = idResiduo)
            } catch (e: Exception){
                Log.e("reporteLogging", "Excepción en processReport $e")
            }
        }
    }

    fun seleccionarTipoResiduo(tipo: TipoResiduo?) {
        _tipoResiduoSeleccionado.value = tipo
    }




}