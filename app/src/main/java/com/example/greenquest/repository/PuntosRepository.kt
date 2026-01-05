package com.example.greenquest.repository

import com.example.greenquest.GreenQuestApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PuntosRepository{
    private val userDao by lazy {
        GreenQuestApp.instance.database.userDao()
    }
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getPuntaje() {
        withContext(ioDispatcher) {
            userDao.getFirstUser()?.puntos ?: 0
        }
    }

    suspend fun addPuntaje(puntosAdd : Int){
        if (puntosAdd <= 0) return
        userDao.incrementarPuntos(puntosAdd)
    }
}