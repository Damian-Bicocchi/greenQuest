package com.example.greenquest.database.trivia

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

class TriviaDataLoader() {
    suspend fun loadTriviaDataFromJson(context: Context): TriviaMetadata {
        return withContext(Dispatchers.IO) {
            try {

                val inputStream: InputStream = context.assets.open("trivia_preguntas.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }

                Gson().fromJson(jsonString, TriviaMetadata::class.java)
            } catch (e: Exception) {
                Log.e("triviaLogging", "Error cargando el json de preguntas: ${e.message}")

                throw Exception("Error cargando el json de preguntas: ${e.message}", e)
            }
        }
    }

    suspend fun getJsonVersion(context: Context): Int {
        return withContext(Dispatchers.IO) {
            try {
                
                val inputStream: InputStream = context.resources.assets.open("trivia_preguntas.json")
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val trivia = gson.fromJson(jsonString, TriviaMetadata::class.java)
                trivia.version
            } catch (e: Exception) {
                Log.e("triviaLogging", "hubo error ${e} al parsear el json")
                -1
            }
        }
    }
}