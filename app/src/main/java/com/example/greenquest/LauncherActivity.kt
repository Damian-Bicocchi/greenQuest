package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.*
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.ui.iniciar_sesion
import com.example.greenquest.ui.menu_principal
import kotlinx.coroutines.*

class LauncherActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                Thread.sleep(100)
                true
            }
        }


        lifecycleScope.launch {
            val accessToken = TokenDataStoreProvider.get().getAccessToken()
            val refreshToken = TokenDataStoreProvider.get().getRefreshToken()
            val usuarioLocal = UsuarioRepository.obtenerUsuarioLocal()

            // Sí, sin este choclo, no cierra la sesión si el token falla en autenticar.
            if (accessToken == null || refreshToken == null || usuarioLocal == null) {
                usuarioLocal?.let { UsuarioRepository.eliminarUsuarioLocal(it) }
                TokenDataStoreProvider.get().clearAllTokens()
                startActivity(Intent(this@LauncherActivity, iniciar_sesion::class.java))
            } else {
                try{
                    startActivity(Intent(this@LauncherActivity, menu_principal::class.java))
                } catch (_: Exception){
                    usuarioLocal.let { UsuarioRepository.eliminarUsuarioLocal(it) }
                    TokenDataStoreProvider.get().clearAllTokens()
                    startActivity(Intent(this@LauncherActivity, iniciar_sesion::class.java))
                }
            }

            finish()
        }
    }
}