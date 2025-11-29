package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class recuperar_contrasenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasenia)

        val restablecerContrasenia = findViewById<View>(R.id.btn_recuperar)
        restablecerContrasenia.setOnClickListener {
            val inputEmail = findViewById<EditText>(R.id.input_email)
            val mensajeError = findViewById<TextView>(R.id.mensaje_error)

            val emailTexto = inputEmail.text.toString().trim()

            if (emailTexto.isEmpty()) {
                mensajeError.visibility = View.VISIBLE
                mensajeError.text = "Debe ingresar un correo válido"
            } else {
                mensajeError.visibility = View.INVISIBLE
                val intent = Intent(this, restablecer_contrasenia::class.java)
                startActivity(intent)
            }
        }
        val cancelar = findViewById<View>(R.id.btn_cancelar)
        cancelar.setOnClickListener {
            val intent = Intent(this, IniciarSesion::class.java)
            startActivity(intent)
        }
    }
}