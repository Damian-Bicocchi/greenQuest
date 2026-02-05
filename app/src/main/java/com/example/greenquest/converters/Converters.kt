package com.example.greenquest.converters

import androidx.room.TypeConverter
import com.example.greenquest.states.reporte.EstadoReporte
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {

            formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromEstadoReporte(estado: EstadoReporte): String {
        return estado.toString()
    }

    @TypeConverter
    fun toEstadoReporte(estadoString: String): EstadoReporte {
        return EstadoReporte.valueOf(estadoString)
    }
}

