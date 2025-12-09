package com.example.greenquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.greenquest.databinding.ActivityIniciarSesionBinding
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import com.example.greenquest.database.AppDatabase
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.viewmodel.InicioSesionModel
import com.example.greenquest.viewmodel.RegistroViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executor


class iniciar_sesion : ComponentActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var viewModel: InicioSesionModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val chequeo = lifecycleScope.launch {
            if (usuarioLocalExiste()) {
                startActivity(Intent(this@iniciar_sesion, menu_principal::class.java))
            }
            finish()
            return@launch
        }
        */

        enableEdgeToEdge()


        viewModel = ViewModelProvider(this).get(InicioSesionModel::class.java)
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recuperarContrasena = binding.linkRecuperarContrasenia
        recuperarContrasena.text = subrayarTexto(recuperarContrasena.text.toString())

        val registrarCuenta = binding.linkRegistrarCuenta
        registrarCuenta.text = subrayarTexto(registrarCuenta.text.toString())

        val botonIniciarSesion = binding.iniciarSesionButton

        registrarCuenta.setOnClickListener {
            val intent = Intent(this, registrar_cuenta::class.java)
            startActivity(intent)
        }

        recuperarContrasena.setOnClickListener {
            val intent = Intent(this, recuperar_contrasenia::class.java)
            startActivity(intent)
        }




    }

    private suspend fun usuarioLocalExiste(): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()
        return usuario != null
    }

    private fun chequeoIniciarSesion() {
        val userName = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        viewModel.iniciarSesion(userName, password).observe(this) { resultado ->
        if (resultado == "OK") {
            Toast.makeText(this, "Inicio de sesión exitoso.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, menu_principal::class.java))
        } else {
            Toast.makeText(this, resultado.toString(), Toast.LENGTH_LONG).show()
        }


        }

    }

    private fun subrayarTexto(texto: String): SpannableString {
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, texto.length, 0)
        return spannableString
    }

    private fun crearVista(){

    }
}
