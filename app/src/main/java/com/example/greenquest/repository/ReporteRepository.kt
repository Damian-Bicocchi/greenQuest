package com.example.greenquest.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.reporte.ReporteData
import com.example.greenquest.states.reporte.EstadoReporte
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.OffsetDateTime

object ReporteRepository {
    private val imageReportDao by lazy {
        GreenQuestApp.instance.database.imageDao()
    }
    private val historialResiduoDao by lazy{
        GreenQuestApp.instance.database.historialResiduoDao()
    }

    suspend fun insertarReporte(context: Context, imageData: Bitmap, clasificacionUsuario: TipoResiduo, idResiduo: String) {
        withContext(Dispatchers.IO) {
            val fileName = "reporte_${idResiduo}_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            FileOutputStream(file).use { out ->
                imageData.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            val fkIdHistorialResiduo = EstadisticasRepository.obtenerIdHistorialDeIdResiduo(idResiduo) ?: return@withContext

            imageReportDao.insertImage(
                ReporteData(
                    idImagenReportada = 0,
                    imageData = file.absolutePath,
                    clasificacionUsuario = clasificacionUsuario,
                    fecha = OffsetDateTime.now(),
                    idUsuarioReporte = UsuarioRepository.obtenerIdUsuarioActual(),
                    idResiduo = idResiduo,
                    fkIdHistorialResiduoReportado = fkIdHistorialResiduo,
                )
            )

            historialResiduoDao.actualizarEstadoReporte(fkIdHistorialResiduo, EstadoReporte.REPORTADO)
        }
    }

    suspend fun actualizarReporte(idResiduo: String, nuevoEstadoReporte: EstadoReporte) {
        withContext(Dispatchers.IO){
            val idHistorial =
                historialResiduoDao.obtenerIdHistorialDeIdResiduo(idResiduo = idResiduo)
                    ?: return@withContext
            historialResiduoDao.actualizarEstadoReporte(
                idHistorial,
                reporteEstado = nuevoEstadoReporte,
            )

            if (nuevoEstadoReporte == EstadoReporte.SIN_REPORTE){
                imageReportDao.deleteImageWithDetails(idResiduo = idResiduo)
            }
        }
    }

    suspend fun obtenerReporte(idResiduo: String): ReporteData? {
        if (idResiduo.isEmpty() || idResiduo.isBlank()) return null
        val reporte = imageReportDao.obtenerReportePorIdResiduo(idResiduo)

        return reporte
    }


}