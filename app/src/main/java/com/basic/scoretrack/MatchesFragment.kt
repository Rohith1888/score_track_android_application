package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class MatchesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cricketCard = view.findViewById<CardView>(R.id.cricket_card)
        val kabaddiCard = view.findViewById<CardView>(R.id.kabaddi_card)

        cricketCard.setOnClickListener {
            navigateToMatchesDetail("Cricket")
        }

        kabaddiCard.setOnClickListener {
            navigateToMatchesDetail("Kabaddi")
        }
    }

    private fun navigateToMatchesDetail(sport: String) {
        val fragment = MatchDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sport)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment)
            .addToBackStack(null)
            .commit()
    }
}
