package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.greenquest.databinding.ActivityIniciarSesionBinding
import android.text.SpannableString
import android.text.style.UnderlineSpan

class iniciar_sesion : ComponentActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar el binding del layout de inicio de sesión y establecerlo como content view
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recuperarContrasena = binding.linkRecuperarContrasenia
        val recuperartextoSubrayado = SpannableString("Recuperar contraseña")
        recuperartextoSubrayado.setSpan(UnderlineSpan(), 0, recuperartextoSubrayado.length, 0)
        recuperarContrasena.text = recuperartextoSubrayado

        val registrarCuenta = binding.linkRegistrarCuenta
        val registrartextoSubrayado = SpannableString("Registrar cuenta")
        registrartextoSubrayado.setSpan(UnderlineSpan(), 0, registrartextoSubrayado.length, 0)
        registrarCuenta.text = registrartextoSubrayado

        registrarCuenta.setOnClickListener {


            val intent = Intent(this, registrar_cuenta::class.java)
            startActivity(intent)
        }

        recuperarContrasena.setOnClickListener {
            val intent = Intent(this, recuperar_contrasenia::class.java)
            startActivity(intent)
        }

        val intent = Intent(this, menu_principal::class.java)

        val botonIniciarSesion = binding.iniciarSesionButton
        botonIniciarSesion.setOnClickListener {
            startActivity(intent)
        }
        fun chequeoIniciarSesion() {
        }
    }
}
