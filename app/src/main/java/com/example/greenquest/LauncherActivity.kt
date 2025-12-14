package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.*
import androidx.lifecycle.*
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.repository.UsuarioRepository.obtenerUsuarioLocal
import com.example.greenquest.ui.iniciar_sesion
import com.example.greenquest.ui.menu_principal
import kotlinx.coroutines.*

class LauncherActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val usuarioLocal = obtenerUsuarioLocal()

            if (usuarioLocal != null) {
               try{
                    val response = UsuarioRepository.login(
                        usuarioLocal.userName!!,
                        usuarioLocal.password!!
                    )

                    if (response.isSuccessful) {
                        startActivity(Intent(this@LauncherActivity, menu_principal::class.java))
                    } else {
                        startActivity(Intent(this@LauncherActivity, iniciar_sesion::class.java))
                    }
                } catch (e: Exception){
                    startActivity(Intent(this@LauncherActivity, iniciar_sesion::class.java))
                }
            } else {
                // NO ACEPTAR ESTE CAMBIO EN UN MERGE
                //startActivity(Intent(this@LauncherActivity, menu_principal::class.java))
                startActivity(Intent(this@LauncherActivity, iniciar_sesion::class.java))

            }

            finish()
        }
    }
}