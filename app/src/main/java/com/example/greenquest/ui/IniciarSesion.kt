package com.example.greenquest.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.greenquest.BuildConfig
import com.example.greenquest.GreenQuestApp
import com.example.greenquest.R
import com.example.greenquest.RetrofitInstance
import com.example.greenquest.TokenDataStoreProvider
import com.example.greenquest.adapters.CategoriaAdapter
import com.example.greenquest.adapters.ServerAdapter
import com.example.greenquest.databinding.ActivityIniciarSesionBinding
import com.example.greenquest.enums.ServerOption
import com.example.greenquest.viewmodel.InicioSesionModel
import kotlinx.coroutines.launch

class IniciarSesion : ComponentActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding
    private lateinit var viewModel: InicioSesionModel
    private lateinit var serverAdapter: ServerAdapter

    private val serverInputDebounceHandler = Handler(Looper.getMainLooper())
    private var serverInputDebounceRunnable: Runnable? = null
    private val DEBOUNCE_DELAY_MS = 500L


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

        serverAdapter = ServerAdapter(this, R.layout.dropdown_item)
        binding.serverSelectionTextView.setAdapter(serverAdapter)
        binding.serverSelectionTextView.setText(
            if (GreenQuestApp.apiStorage.isDefault()) {
                serverAdapter.getItem(0)?.getString(this) ?: ""
            } else {
                binding.serverField.visibility = View.VISIBLE;
                binding.serverInput.setText(GreenQuestApp.apiStorage.getApiURL());
                serverAdapter.getItem(1)?.getString(this) ?: ""
            }, false
        )
        binding.serverSelectionTextView.setOnItemClickListener { _, _, position, _ ->
            val serverOption = serverAdapter.getItem(position) ?: return@setOnItemClickListener
            binding.serverSelectionTextView.setText(
                serverOption.getString(this), false
            )
            when (serverOption) {
                ServerOption.DEFAULT -> {
                    binding.serverField.visibility = View.GONE
                    GreenQuestApp.apiStorage.saveApiURL(BuildConfig.BASE_URL)
                    RetrofitInstance.onApiUrlChanged();
                    Toast.makeText(this, getString(R.string.server_url_saved), Toast.LENGTH_SHORT).show()
                }
                ServerOption.CUSTOM  -> {
                    binding.serverField.visibility = View.VISIBLE
                    val current = binding.serverInput.text?.toString().orEmpty()
                    if (current.isNotBlank()) {
                        GreenQuestApp.apiStorage.saveApiURL(current)
                        RetrofitInstance.onApiUrlChanged();
                        Toast.makeText(this, getString(R.string.server_url_saved), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.serverInput.addTextChangedListener { editable ->
            serverInputDebounceRunnable?.let { serverInputDebounceHandler.removeCallbacks(it) }
            val newRunnable = Runnable {
                val url = editable?.toString().orEmpty()
                if (url.isNotBlank()) {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        GreenQuestApp.apiStorage.saveApiURL(url)
                        RetrofitInstance.onApiUrlChanged();
                        Toast.makeText(this, getString(R.string.server_url_saved), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.invalid_server_url), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            serverInputDebounceRunnable = newRunnable
            serverInputDebounceHandler.postDelayed(newRunnable, DEBOUNCE_DELAY_MS)
        }

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