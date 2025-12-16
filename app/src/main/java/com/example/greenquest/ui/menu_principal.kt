package com.example.greenquest.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.greenquest.R
import com.example.greenquest.databinding.ActivityToolbarBinding
import com.example.greenquest.fragments.MiPerfilFragment
import com.example.greenquest.fragments.CategorizarFragment
import com.example.greenquest.fragments.EscanearFragment
import com.example.greenquest.fragments.TopAmigosFragment
import com.example.greenquest.fragments.TopGlobal
import com.google.android.material.navigation.NavigationBarView

class menu_principal : AppCompatActivity() {

    private val topGlobalFragment = TopGlobal()
    private val TopAmigosFragment = TopAmigosFragment()
    private val escanearFragment = EscanearFragment()

    private val categorizarFragment = CategorizarFragment()

    private val miPefilFragment = MiPerfilFragment()

    private lateinit var binding: ActivityToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.bottomNavigation
        navigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navigation.setOnItemSelectedListener { item -> onNavigationItemSelectedListener(item) }
        loadFragment(topGlobalFragment)
    }

    private val onNavigationItemSelectedListener: (MenuItem) -> Boolean = { item ->
        when (item.itemId) {
            R.id.topGlobalFragment -> {
                loadFragment(topGlobalFragment)
                true
            }

            R.id.topAmigosFragment -> {
                loadFragment(TopAmigosFragment)
                true
            }

            R.id.escanearFragment -> {
                loadFragment(escanearFragment)
                true
            }

            R.id.categorizarFragment-> {
                loadFragment(categorizarFragment)
                true
            }
            R.id.miPefilFragment -> {
                loadFragment(miPefilFragment)
                true
            }

            else -> false
        }
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}