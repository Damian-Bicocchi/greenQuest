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
import com.example.greenquest.repository.UsuarioRepository
import com.example.greenquest.ui.menu_principal
import com.example.greenquest.ui.recuperar_contrasenia
import com.example.greenquest.ui.registrar_cuenta
import com.example.greenquest.viewmodel.InicioSesionModel



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


        registrarCuenta.setOnClickListener {
            val intent = Intent(this, registrar_cuenta::class.java)
            startActivity(intent)
        }

        recuperarContrasena.setOnClickListener {
            val intent = Intent(this, recuperar_contrasenia::class.java)
            startActivity(intent)
        }




    }

    @Deprecated("Sin uso en greenQuest")
    private suspend fun usuarioLocalExiste(): Boolean {
        val usuario = UsuarioRepository.obtenerUsuarioLocal()
        return usuario != null
    }

    @Deprecated("Sin uso en greenQuest")
    private fun chequeoIniciarSesion() {
        val userName = binding.usernameInput.text.toString()
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

}
