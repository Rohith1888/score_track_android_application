package com.basic.scoretrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.basic.scoretrack.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.createAccount.setOnClickListener {
            navigateToRegistration()
        }
        binding.login.setOnClickListener{
            navigateToHome()
        }

        return view
    }

    private fun navigateToRegistration() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }
    private fun navigateToHome() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, HomeFragment())
            .addToBackStack(null)
            .commit()

        // Make Bottom Navigation Visible
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_bar).visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
