package com.example.greenquest.repository

import android.util.Log
import com.example.greenquest.QrPayloadResiduo
import com.example.greenquest.R
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.TipoResiduo
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.example.greenquest.database.DatosEscaneo
import com.example.greenquest.fragments.EscaneadoExitoso
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.serialization.json.Json

object ScannerRepository {
    fun processBarcode(barcode: Barcode) : QrPayloadResiduo {
        val jsonString: String? = barcode.displayValue
        var productInfo: QrPayloadResiduo?
        try {
            // Parsear el JSON del QR
            productInfo = jsonString?.let {
                Json.decodeFromString(it)
            }
        } catch (_: Exception) {
            productInfo = QrPayloadResiduo(
                "", -1, TipoResiduo.DESCONOCIDO
            )
        }
        return productInfo!!

    }

    suspend fun reclamarResiduo(idResiduo: String) : ReclamarResiduoGenericResponse {
        var response : ReclamarResiduoGenericResponse
        var mensajeError = "Residuo no encontrado"
        if (idResiduo.isEmpty() or idResiduo.isBlank()) {
            return ReclamarResiduoGenericResponse("", mensajeError)
        }

        // CONEXIÖN CON LA API
        try {
            val request = ReclamarResiduoRequest(idResiduo = idResiduo)
            response = RetrofitInstance.api.residuoReclamar(request)

            return response

        } catch (e: Exception) {
            return ReclamarResiduoGenericResponse("", e.toString())
        }
    }
}
