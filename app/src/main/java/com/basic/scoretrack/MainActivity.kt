package com.basic.scoretrack

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav_bar)

        if (savedInstanceState == null) {
            loadFragment(MatchesFragment())
            bottomNav.visibility = View.GONE // Hide BottomNav during Login
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_matches -> loadFragment(MatchesFragment())
                R.id.nav_account -> loadFragment(ProfileFragment())
                R.id.nav_teams -> loadFragment(TeamsFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()

        // Show/hide BottomNavigationView based on fragment type
        bottomNav.visibility = if (fragment is LoginFragment || fragment is RegisterFragment) View.GONE else View.VISIBLE
    }

    fun openMatchesFragment(sport: String) {
        val fragment = MatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sport)
            }
        }
        loadFragment(fragment, addToBackStack = true)
    }
}
