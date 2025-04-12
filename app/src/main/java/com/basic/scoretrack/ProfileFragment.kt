package com.basic.scoretrack

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        logoutButton = view.findViewById(R.id.logout_button)

        // Check if the user is logged in via Firebase or SharedPreferences
        loadUserProfile()

        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun loadUserProfile() {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        val fullName = sharedPref.getString("fullName", null)
        val email = sharedPref.getString("email", null)

        // Check if the user is logged in via app backend (SharedPreferences)
        if (fullName != null && email != null) {
            profileName.text = fullName
            profileEmail.text = email
            // Optionally, set a placeholder image if no photo URL
            profileImage.setImageResource(R.drawable.profile)
        } else {
            // If not in SharedPreferences, load user info from Firebase (Google login)
            loadUserProfileGoogle()
        }
    }

    private fun loadUserProfileGoogle() {
        val user: FirebaseUser? = auth.currentUser
        user?.let {
            profileName.text = it.displayName ?: "Unknown User"
            profileEmail.text = it.email ?: "No Email"

            // Load profile image using Glide
            Glide.with(this)
                .load(it.photoUrl) // Fetch profile image URL
                .placeholder(R.drawable.profile_image) // Default placeholder image
                .circleCrop()
                .into(profileImage)
        }
    }

    private fun logoutUser() {
        auth.signOut()

        // Clear the SharedPreferences when the user logs out
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Navigate to LoginFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, LoginFragment())
            .addToBackStack(null)
            .commit()

        // Hide Bottom Navigation Bar
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_bar).visibility = View.GONE
    }
}
