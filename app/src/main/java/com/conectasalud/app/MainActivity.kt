package com.conectasalud.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.conectasalud.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup bottom navigation with navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Control bottom nav visibility based on current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.misTurnosFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }

        // Handle non-functional nav items (search, profile)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.misTurnosFragment -> {
                    navController.navigate(R.id.misTurnosFragment)
                    true
                }
                R.id.nav_search -> {
                    android.widget.Toast.makeText(this, "Próximamente disponible", android.widget.Toast.LENGTH_SHORT).show()
                    false
                }
                R.id.nav_profile -> {
                    android.widget.Toast.makeText(this, "Próximamente disponible", android.widget.Toast.LENGTH_SHORT).show()
                    false
                }
                else -> false
            }
        }
    }
}
