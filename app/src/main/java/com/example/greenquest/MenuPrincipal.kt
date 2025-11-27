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
class MenuPrincipal : AppCompatActivity() {

    private val firstFragment = FirstFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()

    private lateinit var binding: ActivityToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.bottomNavigation
        navigation.setOnItemSelectedListener { item -> onNavigationItemSelectedListener(item) }
        loadFragment(firstFragment)
    }

    private val onNavigationItemSelectedListener: (MenuItem) -> Boolean = { item ->
        when (item.itemId) {
            R.id.firstFragment -> {
                loadFragment(firstFragment)
                true
            }

            R.id.secondFragment -> {
                loadFragment(secondFragment)
                true
            }

            R.id.thirdFragment -> {
                loadFragment(thirdFragment)
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