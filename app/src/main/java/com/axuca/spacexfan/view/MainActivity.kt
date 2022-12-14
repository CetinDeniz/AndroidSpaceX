package com.axuca.spacexfan.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.axuca.spacexfan.R
import com.axuca.spacexfan.databinding.FragmentFavoritesBinding
import com.axuca.spacexfan.view_model.MainVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainVM>()

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.visibility = when (destination.id) {
                R.id.signInFragment -> View.GONE
                R.id.signUpFragment -> View.GONE
                R.id.forgotPasswordFragment -> View.GONE
                R.id.rocketDetailFragment -> View.GONE
                R.id.upcomingDetailFragment -> View.GONE
                else -> View.VISIBLE
            }
        }

        setupActionBarWithNavController(navController)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update navController Start Destination accordingly.
        if (!viewModel.isUserSignedIn()) {
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)

            graph.setStartDestination(R.id.loginGraph)
            navController.graph = graph
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}