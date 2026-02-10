package com.example.greenquest.viewmodel

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.repository.ReporteRepository
import com.example.greenquest.states.reporte.EstadoReporte
import com.example.greenquest.states.reporte.EstadoReporteUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.core.graphics.drawable.toDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ReporteViewModel : ViewModel(){
    private val _reportState = MutableLiveData<EstadoReporte>()
    val reporteState: LiveData<EstadoReporte> = _reportState
    private val _reporteUIState = MutableLiveData<EstadoReporteUI>()
    val reporteUIState: LiveData<EstadoReporteUI> = _reporteUIState


    private val _tipoResiduoSeleccionado = MutableStateFlow<TipoResiduo?>(null)



    fun processReport(idResiduo: String, imageData: Bitmap, context: Context){

        if (!validarReporte(idResiduo, imageData)) return

        viewModelScope.launch {
            try {
                ReporteRepository.insertarReporte(
                    imageData = imageData,
                    clasificacionUsuario = _tipoResiduoSeleccionado.value!!,
                    idResiduo = idResiduo,
                    context = context
                )
                _reporteUIState.value = EstadoReporteUI.Reportado
            } catch (e: Exception){
                Log.e("greenQuest",
                    "Excepción en processReport $e")
            }
        }
    }

    private fun validarReporte(idResiduo: String, imageData: Bitmap): Boolean {
        if (idResiduo.isEmpty()) {
            _reporteUIState.value = EstadoReporteUI.ReporteFallido("No hay " +
                    "un residuo asociado para reportar")
            return false
        }

        if (_tipoResiduoSeleccionado.value == null) {
            _reportState.value = EstadoReporte.REPORTE_FALLIDO
            _reporteUIState.value = EstadoReporteUI.ReporteFallido("No seleccionó " +
                    "una categoría de residuo")

            return false
        }

        if (imageData.width == 0 || imageData.height == 0) {
            _reporteUIState.value = EstadoReporteUI.ReporteFallido("La imagen no " +
                    "es válida")
            return false
        }

        return true
    }

    fun seleccionarTipoResiduo(tipo: TipoResiduo?) {
        _tipoResiduoSeleccionado.value = tipo
    }

    fun resetearEstado(){
        _reportState.value = EstadoReporte.SIN_REPORTE
    }

    fun eliminarReporte(idResiduo: String){
        if (idResiduo.isEmpty()) return
        viewModelScope.launch {
            ReporteRepository.actualizarReporte(idResiduo, EstadoReporte.SIN_REPORTE)
        }
    }

    suspend fun obtenerCategoriaDenunciada(idResiduo: String) : TipoResiduo {
        if (idResiduo.isEmpty()) return TipoResiduo.BASURA
        val reporte = ReporteRepository.obtenerReporte(idResiduo) ?: return TipoResiduo.BASURA
        return reporte.clasificacionUsuario
    }

    suspend fun obtenerImagenReporte(context: Context, idResiduo: String): Drawable? {
        if (idResiduo.isEmpty()) return null

        val reporte = ReporteRepository.obtenerReporte(idResiduo = idResiduo) ?: return null

        val path = reporte.imageData
        if (path.isEmpty()) return null

        return try {
            withContext(Dispatchers.IO) {
                val file = File(path)
                if (!file.exists()) return@withContext null

                // 4. Decode the file into a Bitmap
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                // 5. Convert to Drawable
                // Use context.resources instead of Resources.getSystem() for correct density scaling
                bitmap?.toDrawable(context.resources)
            }
        } catch (e: Exception) {
            Log.e("greenQuest", "Error decoding image file: $e")
            null
        }
    }


}