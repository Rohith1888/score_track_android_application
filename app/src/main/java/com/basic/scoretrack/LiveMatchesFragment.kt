package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LiveMatchesFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_live_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title
        val title = view.findViewById<TextView>(R.id.sportTitle)
        title.text = "$sportType - Live Matches"

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.liveMatchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load and set adapter
        val matches = getLiveMatchesForSport(sportType ?: "Cricket")
        recyclerView.adapter = LiveMatchAdapter(matches)
    }

    companion object {
        fun newInstance(sportType: String) = LiveMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }

    private fun getLiveMatchesForSport(sport: String): List<LiveMatch> {
        return when (sport) {
            "Cricket" -> listOf(
                LiveMatch(
                    stadium = "M. A. Chidambaram Stadium, Chennai",
                    date = "18 March 24",
                    team1 = "CSK",
                    team1LogoRes = R.drawable.csk_logo,
                    team1Score = "147/3 (15.0)",
                    team2 = "MI",
                    team2LogoRes = R.drawable.mi_logo,
                    team2Score = "Yet to bat",
                    matchDecision = "CSK chose to bat",
                    isLive = true
                )

            )
            "Kabaddi" -> listOf(
                LiveMatch(
                    stadium = "Gachibowli Indoor Stadium, Hyderabad",
                    date = "21 March 24",
                    team1 = "U Mumba",
                    team1LogoRes = R.drawable.srh_logo,
                    team1Score = "22",
                    team2 = "Bengal Warriors",
                    team2LogoRes = R.drawable.kkr_logo, // Assuming placeholder
                    team2Score = "17",
                    matchDecision = "U Mumba leading",
                    isLive = true
                )
            )
            else -> emptyList()
        }
    }
}
