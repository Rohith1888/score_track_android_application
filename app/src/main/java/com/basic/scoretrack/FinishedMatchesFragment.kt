package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FinishedMatchesFragment : Fragment() {

    private var sportType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sportType = it.getString("SPORT_TYPE") ?: "Cricket"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_finished_matches, container, false)
    }

    private fun openMatchDetails(match: FinishedMatch) {
        val fragment = FinishedMatchDetailFragment.newInstance(
            match.stadium,
            match.date,
            match.result,
            match.team1,
            match.team2,
            sportType ?: "Cricket",
            match.team1Logo,
            match.team2Logo,
            match.team1Score,
            match.team2Score
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.finishedMatchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val matches = getFinishedMatchesForSport(sportType ?: "Cricket")

        val adapter = FinishedMatchAdapter(matches) { match ->
            openMatchDetails(match)
        }

        recyclerView.adapter = adapter
    }

    private fun getFinishedMatchesForSport(sport: String): List<FinishedMatch> {
        return if (sport == "Cricket") {
            listOf(
                FinishedMatch(
                    "Narendra Modi Stadium, Ahmedabad", "26 May 24", "SRH", R.drawable.srh_logo,
                    "113 (18.3)", "KKR", R.drawable.kkr_logo, "114/2 (10.3)", "KKR won by 8 wickets (57 balls left)"
                ),
                FinishedMatch(
                    "Wankhede Stadium, Mumbai", "18 March 24", "MI", R.drawable.mi_logo,
                    "165/8 (20)", "RCB", R.drawable.rcb_logo, "166/5 (19.2)", "RCB won by 5 wickets"
                )
            )
        } else {
            listOf(
                FinishedMatch(
                    "Gachibowli Indoor Stadium, Hyderabad", "15 March 24", "Bengal Warriors", R.drawable.kkr_logo,
                    "32", "Tamil Thalaivas", R.drawable.rcb_logo, "29", "Bengal Warriors won by 3 points"
                )
            )
        }
    }

    companion object {
        fun newInstance(sportType: String) = FinishedMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }
}
