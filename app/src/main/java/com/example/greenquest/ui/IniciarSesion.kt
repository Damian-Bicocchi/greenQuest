package com.example.greenquest.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.databinding.ActivityIniciarSesionBinding
import com.example.greenquest.viewmodel.InicioSesionModel
import kotlinx.coroutines.launch

class IniciarSesion : ComponentActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var viewModel: InicioSesionModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val token = TokenDataStoreProvider.get().getAccessToken()

            if (!token.isNullOrEmpty()) {
                startActivity(Intent(this@IniciarSesion, MenuPrincipal::class.java))
                finish()
                return@launch
            }

        }

        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[InicioSesionModel::class.java]
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recuperarContrasena = binding.linkRecuperarContrasenia
        recuperarContrasena.text = subrayarTexto(recuperarContrasena.text.toString())

        val registrarCuenta = binding.linkRegistrarCuenta
        registrarCuenta.text = subrayarTexto(registrarCuenta.text.toString())

        val botonIniciarSesion = binding.iniciarSesionButton

        registrarCuenta.setOnClickListener {
            val intent = Intent(this, RegistrarCuenta::class.java)
            startActivity(intent)
        }

        recuperarContrasena.setOnClickListener {
            val intent = Intent(this, RecuperarContrasenia::class.java)
            startActivity(intent)
        }
        botonIniciarSesion.setOnClickListener {
            val userName = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.iniciarSesion(userName, password)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.status.collect { resultado ->
                    resultado ?: return@collect
                    if (resultado == "OK") {
                        Toast.makeText(
                            this@IniciarSesion,
                            "Inicio de sesión exitoso.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@IniciarSesion, MenuPrincipal::class.java))
                    } else {
                        Toast.makeText(this@IniciarSesion, resultado, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    private fun subrayarTexto(texto: String): SpannableString {
        val spannableString = SpannableString(texto)
        spannableString.setSpan(UnderlineSpan(), 0, texto.length, 0)
        return spannableString
    }


}