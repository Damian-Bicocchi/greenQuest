package com.example.greenquest.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.states.reporte.EstadoReporte
import kotlinx.coroutines.launch

class ReporteViewModel : ViewModel(){
    private val _reportState = MutableLiveData(EstadoReporte.SIN_REPORTE)
    val reporteState: LiveData<EstadoReporte> = _reportState
    private val _reporteMensajeFallido = MutableLiveData("")
    val reporteMensajeFallido : LiveData<String> = _reporteMensajeFallido

    fun processReport(idResiduo: String, clasificacionUsuario: TipoResiduo, imageData: Bitmap){
        if (idResiduo.isEmpty()) {
            _reportState.value = EstadoReporte.REPORTE_FALLIDO
            _reporteMensajeFallido.value = "Residuo vacío"
        }

        viewModelScope.launch {
            try {

            } catch (e: Exception){
                Log.e("reporteLogging", "Excepción en processReport $e")
            }
        }
    }




}