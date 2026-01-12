package com.example.greenquest.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.greenquest.R
import com.example.greenquest.databinding.ActivityToolbarBinding
import com.example.greenquest.fragments.MiPerfilFragment
import com.example.greenquest.fragments.CategorizarFragment
import com.example.greenquest.fragments.EscanearFragment
import com.example.greenquest.fragments.EstadisticasFragment
import com.example.greenquest.fragments.TiendaFragment
import com.example.greenquest.fragments.TopGlobal
import com.example.greenquest.fragments.TriviaFragment
import com.google.android.material.navigation.NavigationBarView

class menu_principal : AppCompatActivity() {

    private lateinit var binding: ActivityToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.bottomNavigation
        navigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        // NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.frame_container) as NavHostFragment

        val navController = navHostFragment.navController

        // Conectar BottomNavigation con el grafo
        binding.bottomNavigation.setupWithNavController(navController)

        // Click en perfil (toolbar)
        binding.miPerfil.setOnClickListener {
            navController.navigate(R.id.miPerfil)
        }

        // Toolbar dinámica según destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.topGlobalFragment -> setToolbar("Top Global", true)
                R.id.tiendaFragment -> setToolbar("Tienda", true)
                R.id.categorizarFragment -> setToolbar("Categorizar", true)
                R.id.triviaFragment -> setToolbar("Trivia", true)
                R.id.miPerfil-> setToolbar("Mi Perfil", true)
                R.id.escanearFragment -> setToolbar("", false)
                R.id.estadisticasFragment -> setToolbar("Estadísticas de usuario", true)
                else -> setToolbar("", true)
            }
        }
    }

    private fun setToolbar(titulo: String, mostrarToolbar: Boolean = true) {
        binding.toolbarContainer.visibility =
            if (mostrarToolbar) View.VISIBLE else View.GONE
        binding.nombreFragmentActualTextView.text = titulo
    }
}
