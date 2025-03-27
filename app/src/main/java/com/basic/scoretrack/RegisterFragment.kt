package com.basic.scoretrack

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.basic.scoretrack.databinding.FragmentRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.signup.setOnClickListener {
            val (fullName, email, password) = getValidatedFields() ?: return@setOnClickListener

            // Proceed with registration logic (e.g., Retrofit API)
            val user = User(fullName, email, password)

            RetrofitClient.instance.registerUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                        navigateToLogin() // Navigate instead of using finish()
                    } else {
                        Toast.makeText(requireContext(), "Registration failed!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.signInLink.setOnClickListener {
            navigateToLogin()
        }

        return view
    }

    private fun getValidatedFields(): Triple<String, String, String>? {
        val fullName = binding.fullName.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (fullName.isEmpty()) {
            binding.fullName.error = "Full Name is required"
            return null
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Enter a valid email"
            return null
        }

        if (password.isEmpty() || password.length < 6) {
            binding.password.error = "Password must be at least 6 characters"
            return null
        }

        return Triple(fullName, email, password)
    }

    private fun navigateToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, LoginFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


