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


    private suspend fun obtenerResiduosParaRango(
        inicio: OffsetDateTime?,
        fin: OffsetDateTime?,
        idUsuario: Int
    ): Map<TipoResiduo, Int> {
        val listaResumen: List<ResumenResiduo> = historialResiduoDao.obtenerResiduosEntreFechas(
            inicio,
            fin,
            idUsuario
        )
        val mapARetornar: HashMap<TipoResiduo, Int> = HashMap()
        for (elemento in listaResumen) {
            mapARetornar[elemento.tipo_residuo] = elemento.total
        }
        return mapARetornar
    }


    suspend fun obtenerResiduosEnRangoFecha(
        periodo: PeriodoResiduo,
        idUsuario: Int
    ): Map<TipoResiduo, Int>{
        val fechaAhora = Instant.now()
        val fechaHoy = LocalDate.now()
        
        return when(periodo){
            PeriodoResiduo.HOY -> {
                val inicioDeResumen = fechaHoy.atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = OffsetDateTime.ofInstant(fechaAhora, ZoneOffset.UTC)
                obtenerResiduosParaRango(inicioDeResumen, finDeResumen, idUsuario)
            }

            PeriodoResiduo.SEMANA -> {
                val inicioDeResumen = fechaHoy.minusDays(6).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerResiduosParaRango(inicioDeResumen, finDeResumen, idUsuario)

            }
            PeriodoResiduo.MES -> {
                val inicioDeResumen = fechaHoy.withDayOfMonth(1).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerResiduosParaRango(inicioDeResumen, finDeResumen, idUsuario)

            }
            PeriodoResiduo.AÑO -> {
                val inicioDeResumen = fechaHoy.withDayOfYear(1).atStartOfDay().atOffset(ZoneOffset.UTC)
                val finDeResumen = fechaHoy.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                obtenerResiduosParaRango(inicioDeResumen, finDeResumen, idUsuario)
            }

            PeriodoResiduo.TOTAL -> {
                obtenerResiduosParaRango(null, null, idUsuario)
            }
        }
    }
    
    suspend fun obtenerPuntajeEnRangoFecha(
        periodo: PeriodoResiduo,
        idUsuario: Int
    ): Int{
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
            PeriodoResiduo.TOTAL -> {
                obtenerPuntosParaRango(null, null, idUsuario = idUsuario)
            }
            
        }
    }

    private suspend fun obtenerPuntosParaRango(
        inicio: LocalDate?,
        fin: LocalDate?,
        idUsuario: Int
    ): Int {
        return withContext(Dispatchers.IO) {
            val cantidadPuntos: Int = historialResiduoDao.obtenerCantidadPuntosEntreFechas(
                inicio?.atStartOfDay()?.atOffset(ZoneOffset.UTC),
                fin?.atTime(LocalTime.MAX)?.atOffset(ZoneOffset.UTC),
                idUsuario
            )

            cantidadPuntos
        }
    }


}