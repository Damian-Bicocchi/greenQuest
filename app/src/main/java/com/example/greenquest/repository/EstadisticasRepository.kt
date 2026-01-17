package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.apiParameters.TipoResiduo
import com.example.greenquest.database.escaneo.QrPayloadResiduo
import com.example.greenquest.database.estadisticas.HistorialResiduo
import com.example.greenquest.database.estadisticas.PeriodoResiduo
import com.example.greenquest.database.estadisticas.ResumenPuntos
import com.example.greenquest.database.estadisticas.ResumenResiduo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC).with(LocalTime.MAX)
        }

        return withContext(Dispatchers.IO) {
            val listaResumen: List<ResumenResiduo> = historialResiduoDao.obtenerResiduosEntreFechas(
                inicio,
                fin,
                idUsuario
            )
            val mapARetornar : HashMap<TipoResiduo, Int> = HashMap()
            for (elemento in listaResumen){
                mapARetornar[elemento.tipo_residuo] = elemento.total
            }
            mapARetornar
        }
    }


    suspend fun obtenerPuntajeEnRangoFecha(
        periodo: PeriodoResiduo,
        idUsuario: Int
    ): Map<String, Int> {
        val fechaAhora = Instant.now()
        val fechaHoy = LocalDate.now()

        return when (periodo) {
            PeriodoResiduo.HOY -> {
                val inicioDeResumen = fechaHoy.atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = OffsetDateTime.ofInstant(fechaAhora, ZoneOffset.UTC)
                obtenerPuntosParaRango(inicioDeResumen.toLocalDate(), finDeResumen.toLocalDate(), idUsuario)
            }

            PeriodoResiduo.SEMANA -> {
                val inicioDeResumen = fechaHoy.minusDays(6).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerPuntosParaRango(inicioDeResumen.toLocalDate(), finDeResumen.toLocalDate(), idUsuario)
            }

            PeriodoResiduo.MES -> {
                val inicioDeResumen = fechaHoy.withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerPuntosParaRango(inicioDeResumen.toLocalDate(), finDeResumen.toLocalDate(), idUsuario)
            }

            PeriodoResiduo.AÑO -> {
                val inicioDeResumen = fechaHoy.withDayOfYear(1).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerPuntosParaRango(inicioDeResumen.toLocalDate(), finDeResumen.toLocalDate(), idUsuario)
            }
        }
    }

    private suspend fun obtenerPuntosParaRango(
        inicio: LocalDate,
        fin: LocalDate,
        idUsuario: Int
    ): Map<String, Int> {
        return withContext(Dispatchers.IO) {
            val listaResumenPuntos: List<ResumenPuntos> = historialResiduoDao.obtenerPuntosEntreFechas(
                inicio.atStartOfDay().atOffset(ZoneOffset.UTC),
                fin.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC),
                idUsuario
            )

            val datosObtenidos = listaResumenPuntos.associate { it.fecha to it.total }
            val resultado = mutableMapOf<String, Int>()
            var fechaActual = inicio

            while (!fechaActual.isAfter(fin)) {
                val fechaStr = fechaActual.format(DateTimeFormatter.ISO_LOCAL_DATE)
                resultado[fechaStr] = datosObtenidos[fechaStr] ?: 0
                fechaActual = fechaActual.plusDays(1)
            }
            resultado
        }
    }


}