package com.example.greenquest.repository

import com.example.greenquest.QrPayloadResiduo
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.TipoResiduo
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoGenericResponse
import com.example.greenquest.apiParameters.scanning.ReclamarResiduoRequest
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.IOException

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

    suspend fun reclamarResiduo(idResiduo: String): ReclamarResiduoGenericResponse {

        if (idResiduo.isBlank()) {
            return ReclamarResiduoGenericResponse(
                "",
                "Residuo no encontrado"
            )
        }

        return try {
            val request = ReclamarResiduoRequest(idResiduo = idResiduo)

            val response = RetrofitInstance.api.residuoReclamar(request)

            if (response.isSuccessful) {
                response.body() ?: ReclamarResiduoGenericResponse(
                    response.message(),
                    ""
                )
            } else {
                val errorBody = response.errorBody()?.string()

                val errorMessage = try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("error", "Error desconocido")
                } catch (e: Exception) {
                    "Error desconocido"
                }

                ReclamarResiduoGenericResponse("", errorMessage)
            }

        } catch (e: IOException) {
            ReclamarResiduoGenericResponse(
                "",
                "Error de conexión con el servidor"
            )

        } catch (e: Exception) {
            ReclamarResiduoGenericResponse(
                "",
                "Error inesperado: ${e.message}"
            )
        }
    }

}
