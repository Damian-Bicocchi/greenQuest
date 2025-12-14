package com.example.greenquest.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.databinding.ActivityRegistrarCuentaBinding
import com.example.greenquest.viewmodel.RegistroModel

class registrar_cuenta : AppCompatActivity() {

    private lateinit var viewModel: RegistroModel
    private lateinit var binding: ActivityRegistrarCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrarCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(RegistroModel::class.java)

        val botonRegistrarCuenta = binding.registrarCuentaButton
        val botonCancelar = binding.cancelarRegistro
        botonCancelar.setOnClickListener {
            val intent = Intent(this, iniciar_sesion::class.java)
            startActivity(intent)
        }

        botonRegistrarCuenta.setOnClickListener {
            val username = binding.registroUsernameInput.text.toString()
            val password = binding.registroPasswordInput.text.toString()
            val confirm = binding.registroRepeatPasswordInput.text.toString()
            registrarCuenta(username, password, confirm)
        }

    }
    private fun registrarCuenta(username: String, password: String, confirm: String) {
        viewModel.registrar( username, password ,confirm).observe(this) { resultado ->
            if (resultado == "OK") {
                Toast.makeText(this, "Registro exitoso. Por favor, inicie sesión.", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, iniciar_sesion::class.java))
            } else {
                Toast.makeText(this, resultado, Toast.LENGTH_LONG).show()
            }
        }
    }
}