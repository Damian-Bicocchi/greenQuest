package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.*
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.ui.IniciarSesion
import com.example.greenquest.ui.MenuPrincipal
import kotlinx.coroutines.*

class LauncherActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                usuarioLocal?.let { UsuarioRepository.desactivarSesionUsuarioLocal(it) }
                TokenDataStoreProvider.get().clearAllTokens()
                startActivity(Intent(this@LauncherActivity, IniciarSesion::class.java))
            } else {
                try{
                    startActivity(Intent(this@LauncherActivity, MenuPrincipal::class.java))
                } catch (_: Exception){
                    usuarioLocal.let { UsuarioRepository.desactivarSesionUsuarioLocal(it) }
                    TokenDataStoreProvider.get().clearAllTokens()
                    startActivity(Intent(this@LauncherActivity, IniciarSesion::class.java))
                }
            }

            finish()
        }
    }
}