package com.example.greenquest

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.greenquest.ui.menu_principal
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

class ErrorHandlerProvider(private val appContext: Context) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e("greenQuest", "Error fatal en thread: $t", e)


        runBlocking {
            TokenDataStoreProvider.get().clearAllTokens()
        }

        val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
        exitProcess(0)
    }

    fun logOutAndBackToMenu(t: Thread, e: Throwable) {
        Log.e("greenQuest", "Error en thread: $t", e)

        runBlocking {
            TokenDataStoreProvider.get().clearAllTokens()
        }

        val intent = appContext.packageManager.getLaunchIntentForPackage(appContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
        exitProcess(0)
    }
}