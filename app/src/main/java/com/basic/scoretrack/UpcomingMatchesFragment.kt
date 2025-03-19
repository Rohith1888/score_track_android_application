package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UpcomingMatchesFragment : Fragment() {

    private var sportType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sportType = it.getString("SPORT_TYPE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.matchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matches = getMatchesForSport(sportType ?: "Cricket")

        val adapter = MatchAdapter(matches) { match ->
            openMatchDetails(match)
        }

        recyclerView.adapter = adapter
    }

    private fun openMatchDetails(match: Match) {
        val fragment = UpcomingMatchDetailFragment.newInstance(
            match.stadium,
            match.date,
            match.time,
            match.team1,
            match.team1Logo,
            match.team2,
            match.team2Logo,
            sportType?:"Cricket"
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment) // Ensure this ID exists in the activity layout
            .addToBackStack(null)
            .commit()
    }

    private fun getMatchesForSport(sport: String): List<Match> {
        return if (sport == "Cricket") {
            listOf(
                Match("Eden Gardens, Kolkata", "22 March, Saturday", "07:30 PM", "KKR", R.drawable.kkr_logo, "RCB", R.drawable.rcb_logo),
                Match("Rajiv Gandhi International Stadium, Hyderabad", "23 March, Sunday", "03:30 PM", "SRH", R.drawable.srh_logo, "RR", R.drawable.rr_logo),
                Match("MA Chidambaram Stadium, Chennai", "23 March, Sunday", "07:30 PM", "CSK", R.drawable.csk_logo, "MI", R.drawable.mi_logo)
            )
        } else {
            listOf(
                Match("Gachibowli Indoor Stadium, Hyderabad", "25 March, Monday", "06:00 PM", "Telugu Titans", R.drawable.kkr_logo, "Patna Pirates", R.drawable.srh_logo),
                Match("Kanteerava Indoor Stadium, Bangalore", "26 March, Tuesday", "08:00 PM", "Bengaluru Bulls", R.drawable.rcb_logo, "Dabang Delhi", R.drawable.mi_logo)
            )
        }
    }

    companion object {
        fun newInstance(sportType: String) = UpcomingMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }
}
