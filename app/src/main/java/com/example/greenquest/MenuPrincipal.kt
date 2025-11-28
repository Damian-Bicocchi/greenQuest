package com.example.greenquest

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.greenquest.databinding.ActivityToolbarBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.bottomnavigation.    LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView

class MenuPrincipal : AppCompatActivity() {

    private val topGlobalFragment = TopGlobalFragment()
    private val TopAmigosFragment = TopAmigosFragment()
    private val escanearFragment = EscanearFragment()

    private val categorizarFragment = CategorizarFragment()

    private val amigosFragment = AmigosFragment()

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
            R.id.amigosFragment -> {
                loadFragment(amigosFragment)
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