package com.example.greenquest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.greenquest.R

class restablecer_contrasenia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_restablecer_contrasenia)
        val cancelar = findViewById<View>(R.id.btn_cancelar)
        cancelar.setOnClickListener {
            val intent = Intent(this, iniciar_sesion::class.java)
            startActivity(intent)
        }
    }
}