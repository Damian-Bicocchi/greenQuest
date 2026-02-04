package com.example.greenquest.repository

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.reporte.ReporteData
import com.example.greenquest.states.reporte.EstadoReporte
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.OffsetDateTime

object ReporteRepository {
    private val imageReportDao by lazy {
        GreenQuestApp.instance.database.imageDao()
    }
    private val historialResiduoDao by lazy{
        GreenQuestApp.instance.database.historialResiduoDao()
    }

    suspend fun insertarReporte(imageData: Bitmap, clasificacionUsuario: TipoResiduo, idResiduo: String){

        withContext(Dispatchers.IO){
            val baos = ByteArrayOutputStream()
            imageData.compress(Bitmap.CompressFormat.PNG, 100, baos)

            val fkIdHistorialResiduo =
                EstadisticasRepository.obtenerIdHistorialDeIdResiduo(idResiduo = idResiduo)
                    ?: return@withContext
            imageReportDao.insertImage(
                ReporteData(
                    idImagenReportada = 0,
                    imageData = baos.toByteArray(),
                    clasificacionUsuario = clasificacionUsuario,
                    fecha = OffsetDateTime.now(),
                    idUsuarioReporte = UsuarioRepository.obtenerIdUsuarioActual(),
                    id_residuo = idResiduo,
                    fkIdHistorialResiduoReportado = fkIdHistorialResiduo,
                )
            )

            historialResiduoDao.actualizarEstadoReporte(
                fkIdHistorialResiduo, EstadoReporte.REPORTADO
            )

            Log.d("reporteLogging", "Se acaba de crear el reporte")
        }
    }




}