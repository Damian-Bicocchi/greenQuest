package com.example.greenquest.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.greenquest.R
import com.example.greenquest.databinding.ActivityToolbarBinding
import com.google.android.material.navigation.NavigationBarView

class menu_principal : AppCompatActivity() {

    private lateinit var binding: ActivityToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.bottomNavigation
        navigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.frame_container) as NavHostFragment

        val navController = navHostFragment.navController

        // Conectar BottomNavigation con el grafo
        binding.bottomNavigation.setupWithNavController(navController)

        binding.miPerfil.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(navController.graph.startDestinationId, inclusive = false, saveState = true)
                .build()
            navController.navigate(R.id.miPerfil, null, navOptions)

            // 2) si quedó parado en Configuración, volvés al root (Perfil)
            if (navController.currentDestination?.id == R.id.configuracionFragment) {
                navController.popBackStack(R.id.miPerfil, false)
            }
            try {
                navController.navigate(R.id.miPerfil, null, navOptions)
            } catch (_: Exception) {
                navController.navigate(R.id.miPerfil)
            }
        }
        binding.map.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(navController.graph.startDestinationId, inclusive = false, saveState = true)
                .build()

            try {
                navController.navigate(R.id.mapFragment, null, navOptions = navOptions)
            } catch (_:Exception) {
                navController.navigate(R.id.mapFragment)
            }
        }

        // Toolbar dinámica según destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.topGlobalFragment -> setToolbar("Top Global")
                R.id.tiendaFragment -> setToolbar("Tienda")
                R.id.categorizarFragment -> setToolbar("Categorizar")
                R.id.triviaFragment -> setToolbar("Trivia")
                R.id.miPerfil-> setToolbar("Mi Perfil")
                R.id.escanearFragment -> setToolbar("Escanear", false)
                R.id.estadisticasFragment -> setToolbar("Estadísticas de usuario")
                R.id.configuracionFragment -> setToolbar("Configuración")
                R.id.escaneadoExitosoFragment -> setToolbar("Escaneado exitoso")
                R.id.historialResiduoCompletoFragment -> setToolbar("Historial de reciclado")
                R.id.reportarFragment -> setToolbar("Reportar clasificación")
                R.id.mapFragment -> setToolbar("Mapa de contenedores")
                R.id.informacionReporte -> setToolbar("Informe de reporte")
                else -> setToolbar("")
            }
        }
    }

    fun setToolbar(titulo: String, mostrarToolbar: Boolean = true) {

        binding.toolbarContainer.visibility = if (mostrarToolbar) View.VISIBLE else View.GONE
        binding.nombreFragmentActualTextView.text = titulo
        if (titulo.length >= 20) binding.nombreFragmentActualTextView.textSize = 26f
        else binding.nombreFragmentActualTextView.textSize = 28f
    }
}
