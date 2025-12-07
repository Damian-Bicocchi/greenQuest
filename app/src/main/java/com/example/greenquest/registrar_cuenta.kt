package com.example.greenquest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.greenquest.viewmodel.RegistroViewModel


class registrar_cuenta : AppCompatActivity() {

    private lateinit var viewModel: RegistroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_registrar_cuenta)



        viewModel = ViewModelProvider(this).get(RegistroViewModel::class.java)

        val botonRegistrarCuenta = findViewById<android.widget.Button>(R.id.registrar_cuenta_button)
        val botonCancelar = findViewById<android.widget.Button>(R.id.cancelar_registro)
        botonCancelar.setOnClickListener {
            val intent = android.content.Intent(this, iniciar_sesion::class.java)
            startActivity(intent)
        }

        botonRegistrarCuenta.setOnClickListener {
            val email = findViewById<android.widget.EditText>(R.id.registro_email_input).text.toString()
            val password = findViewById<android.widget.EditText>(R.id.registro_password_input).text.toString()
            val confirm = findViewById<android.widget.EditText>(R.id.registro_repeat_password_input).text.toString()
            registrarCuenta(email, password, confirm)
        }

    }
    private fun registrarCuenta(email: String, password: String, confirm: String) {
        viewModel.registrar( email, password ,confirm).observe(this) { resultado ->
            if (resultado == "OK") {
                val intent = android.content.Intent(this, iniciar_sesion::class.java)
                startActivity(intent)
            } else {
                android.widget.Toast.makeText(this, resultado, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }
}