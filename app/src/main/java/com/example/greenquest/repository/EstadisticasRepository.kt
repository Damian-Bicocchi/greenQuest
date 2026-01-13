package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.escaneo.QrPayloadResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.estadisticas.ResumenResiduo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

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
                    fecha = OffsetDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")),
                    tipoResiduo = payload.tipo_residuo,
                    puntosDados = payload.puntaje
                )
            )
        }
    }

    @Deprecated("Sin uso en la aplicación greenQuest")
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

    suspend fun obtenerResiduosEntre(
        fechaInicio: Long?,
        fechaFin: Long?,
        idUsuario: Int
    ): Map<TipoResiduo, Int> {

        val inicio = fechaInicio?.let {
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }
        val fin = fechaFin?.let {
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
        }

        return withContext(Dispatchers.IO) {
            val listaResumen: List<ResumenResiduo> = historialResiduoDao.obtenerResiduosEntreFechas(
                inicio,
                fin,
                idUsuario
            )
            var mapARetornar : HashMap<TipoResiduo, Int> = HashMap()
            for (elemento in listaResumen){
                mapARetornar[elemento.tipo_residuo] = elemento.total
            }
            mapARetornar
        }
    }


}