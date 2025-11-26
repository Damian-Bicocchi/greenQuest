package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.greenquest.databinding.ActivityIniciarSesionBinding

class IniciarSesion : ComponentActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar el binding del layout de inicio de sesión y establecerlo como content view
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MenuPrincipal::class.java)

        val botonIniciarSesion = binding.iniciarSesionButton
        botonIniciarSesion.setOnClickListener {
            startActivity(intent)
        }
        fun chequeoIniciarSesion() {
        }
    }
}
