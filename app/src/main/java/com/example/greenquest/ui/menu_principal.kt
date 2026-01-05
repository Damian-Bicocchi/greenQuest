package com.example.greenquest.ui

import android.os.Bundle
import android.support.v4.os.IResultReceiver
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
import com.example.greenquest.fragments.TriviaFragment
import com.google.android.material.navigation.NavigationBarView

class menu_principal : AppCompatActivity() {
    private lateinit var binding: ActivityToolbarBinding
    private var topGlobalFragment: TopGlobal? = null
    private var topAmigosFragment: TopAmigosFragment? = null
    private var escanearFragment: EscanearFragment? = null
    private var categorizarFragment: CategorizarFragment? = null
    private var miPefilFragment : MiPerfilFragment? = null

    private var triviaFragment : TriviaFragment? = null

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
                    topAmigosFragment = TopAmigosFragment()
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
            R.id.categorizarFragment -> {
                if (categorizarFragment == null) {
                    categorizarFragment = CategorizarFragment()
                }
                showFragment(categorizarFragment!!)
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

        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { hide(it) }

            if (fragment.isAdded) {
                show(fragment)
            } else {
                add(R.id.frame_container, fragment)
            }

            commit()
        }

        currentFragment = fragment
    }
}