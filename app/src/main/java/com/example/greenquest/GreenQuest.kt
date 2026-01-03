package com.example.greenquest

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.greenquest.database.AppDatabase
import com.example.greenquest.repository.TriviaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
/*
* SupervisorJob():
Es un tipo especial de Job que permite que las corrutinas fallen independientemente
Si una corrutina falla (lanza excepción), no cancela las demás corrutinas del mismo scope
* Dispatchers.DEfault
Es el dispatcher por defecto para operaciones intensivas de CPU
Usa un pool de hilos optimizado para cálculos
* El más los combina, creando un contexto de corrutina
* Lo necesitamos para buena práctica, ya que si lo lanzamos en un GlobalScope asi no más, no sabemos
quién controla su ciclo de vida
 */
private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


class GreenQuestApp : Application() {
    companion object {
        lateinit var instance: GreenQuestApp
            private set
        lateinit var prefs: Prefs
    }

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        TokenDataStoreProvider.init(this)

        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "usuarios-db"
        ).fallbackToDestructiveMigration(false).build()

        prefs = Prefs(applicationContext)

        applicationScope.launch {
            TriviaRepository.inicializarData(applicationContext)
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }

}

