package com.example.greenquest.repository

import android.graphics.Bitmap
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.escaneo.QrPayloadResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.reporte.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.OffsetDateTime

object ReporteRepository {
    private val imageReportDao by lazy {
        GreenQuestApp.instance.database.imageDao()
    }

    suspend fun insertarReporte(imageData: Bitmap, clasificacionUsuario: TipoResiduo, idHistorialResiduo: Long){
        val stream = ByteArrayOutputStream()
        imageData.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        withContext(Dispatchers.IO){
            imageReportDao.insertImage(
                ImageModel(
                    idImagenReportada = 0,
                    imageData = byteArray,
                    clasificacionUsuario = clasificacionUsuario,
                    fecha = OffsetDateTime.now(),
                    idUsuarioReporte = UsuarioRepository.obtenerIdUsuarioActual(),
                    idHistorialResiduoReportado = idHistorialResiduo
                )
            )
        }
    }


}