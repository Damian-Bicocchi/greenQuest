package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.database.escaneo.QrPayloadResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime

object EstadisticasRepository {
    private val historialResiduoDao by lazy {
        GreenQuestApp.instance.database.historialResiduoDao()
    }
    suspend fun insertarResiduoAlHistorial(payload: QrPayloadResiduo){
        withContext(Dispatchers.IO){
            historialResiduoDao.insertarResiduoAlHistorial(
                HistorialResiduo(
                    historialResiduoId = 0,
                    idResiduo = payload.id_residuo,
                    idUsuario = UsuarioRepository.getUserProfile().id!!,
                    fecha = OffsetDateTime.now(),
                    tipoResiduo = payload.tipo_residuo,
                    puntosDados = payload.puntaje
                )
            )
        }
    }

    suspend fun obtenerNResiduosMasRecientes(cantidad: Int, idUsuario: Int): List<HistorialResiduo>{
        if (cantidad <= 0) return emptyList()
        val lista = withContext(Dispatchers.IO){
            return@withContext historialResiduoDao.obtenerNResiduosMasRecientes(cantidad, idUsuario)
        }
        return lista
    }

    suspend fun obtenerTodosLosResiduos(idUsuario: Int): List<HistorialResiduo>{
        val lista = withContext(Dispatchers.IO){
            return@withContext historialResiduoDao.obtenerTodosLosResiduos(
                idUsuario = idUsuario
            )
        }
        return lista
    }
}