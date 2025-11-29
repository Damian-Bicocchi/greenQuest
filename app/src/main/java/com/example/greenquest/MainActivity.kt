package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)


        val iniciarSesion = findViewById<View>(R.id.iniciar_sesion_button)
        iniciarSesion.setOnClickListener {
            // Aquí va la lógica de login
        }

        val linkRecuperar = findViewById<TextView>(R.id.link_recuperar_contrasenia)
        linkRecuperar.setOnClickListener {
            val intent = Intent(this, recuperar_contrasenia::class.java)
            startActivity(intent)
        }
    }
}
