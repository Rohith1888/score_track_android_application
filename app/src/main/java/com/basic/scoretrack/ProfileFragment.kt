package com.basic.scoretrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton: Button = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, LoginFragment()) // Navigate to LoginFragment
            .addToBackStack(null) // Enables back navigation
            .commit()

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_bar).apply {
            visibility = View.GONE  // Hide BottomNavigationView
              // Set Home tab as selected
        }
    }
}
