package com.example.greenquest.repository

import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.Estacion

object EstacionRepository {
    private val api = RetrofitInstance.api

    suspend fun listStations(): List<Estacion> {
        return try {
            api.stations()
        } catch (_: Exception) { // TODO: Manejo de excepciones nula
            listOf()
        }
    }

    suspend fun station(idStation: Integer): Estacion? {
        return try {
            api.station(idStation)
        } catch (_: Exception) {
            null
        }
    }
}