package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.apiParameters.Estacion

object EstacionRepository {
    private val api = RetrofitInstance.api
    private val errorHandler = GreenQuestApp.instance.errorHandler

    suspend fun listStations(): List<Estacion> {
        return try {
            api.stations()
        } catch (e: Exception) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            emptyList()
        }
    }

    suspend fun station(idStation: Int): Estacion {
        return try {
            api.station(idStation)
        } catch (e: Exception) {
            errorHandler.logOutAndBackToMenu(Thread.currentThread(), e)
            Estacion(0,"",.0,.0)
        }
    }
}