package com.example.greenquest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.greenquest.R

class recuperar_contrasenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contrasenia)

        val restablecerContrasenia = findViewById<View>(R.id.btn_recuperar)
        restablecerContrasenia.setOnClickListener {
            val inputUsername = findViewById<EditText>(R.id.input_username)
            val mensajeError = findViewById<TextView>(R.id.mensaje_error)

            val emailTexto = inputUsername.text.toString().trim()

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
            val intent = Intent(this, iniciar_sesion::class.java)
            startActivity(intent)
        }
    }
}