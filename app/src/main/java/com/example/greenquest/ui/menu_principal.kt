package com.example.greenquest.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    private var topGlobalFragment: TopGlobal? = null
    private var topAmigosFragment: TiendaFragment? = null
    private var escanearFragment: EscanearFragment? = null
    private var categorizarFragment: CategorizarFragment? = null
    private var miPefilFragment : MiPerfilFragment? = null

    private var triviaFragment : TriviaFragment? = null

    private var estadisticasFragment : EstadisticasFragment? = null

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.bottomNavigation
        navigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navigation.setOnItemSelectedListener { item -> onNavigationItemSelectedListener(item) }
        val miPerfil = binding.miPerfil
        miPerfil.setOnClickListener {
            miPefilFragment = MiPerfilFragment()
            showFragment(miPefilFragment!!)
        }
        if (savedInstanceState == null) {
            topGlobalFragment = TopGlobal()
            showFragment(topGlobalFragment!!)
            navigation.selectedItemId = R.id.topGlobalFragment
        }
    }

    private val onNavigationItemSelectedListener: (MenuItem) -> Boolean = { item ->
        when (item.itemId) {
            R.id.topGlobalFragment -> {
                if (topGlobalFragment == null) {
                    topGlobalFragment = TopGlobal()
                }
                showFragment(topGlobalFragment!!)
                true
            }
            R.id.topAmigosFragment -> {
                if (topAmigosFragment == null) {
                    topAmigosFragment = TiendaFragment()
                }
                showFragment(topAmigosFragment!!)
                true
            }
            R.id.escanearFragment -> {
                if (escanearFragment == null) {
                    escanearFragment = EscanearFragment()
                }
                showFragment(escanearFragment!!)
                true
            }
            R.id.estadisticaFragment -> {
                if (estadisticasFragment == null) {
                    estadisticasFragment = EstadisticasFragment()
                }
                showFragment(estadisticasFragment!!)
                true
            }
            R.id.triviaFragment -> {
                if (triviaFragment == null) {
                    triviaFragment = TriviaFragment()
                }
                showFragment(triviaFragment!!)
                true
            }

            else -> false
        }
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment == currentFragment) return

        // ✅ Actualizar toolbar ANTES o DESPUÉS del commit (da igual mientras sea siempre)
        when (fragment) {
            is TopGlobal -> setToolbar("Top Global", true)
            is TiendaFragment -> setToolbar("Tienda", true)
            is CategorizarFragment -> setToolbar("Categorizar", true)
            is TriviaFragment -> setToolbar("Trivia", true)
            is MiPerfilFragment -> setToolbar("Mi Perfil", true)
            is EscanearFragment -> setToolbar("", false)
            is EstadisticasFragment -> setToolbar("Estadísticas de usuario", true)
            else -> setToolbar("", true)
        }

        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { hide(it) }

            if (fragment.isAdded) show(fragment)
            else add(R.id.frame_container, fragment)

            commit()
        }

        currentFragment = fragment
    }

    private fun setToolbar(titulo : String, mostrarToolbar : Boolean = true){
        binding.toolbarContainer.visibility = if (mostrarToolbar) View.VISIBLE else View.GONE
        binding.nombreFragmentActualTextView.text = titulo
    }
}