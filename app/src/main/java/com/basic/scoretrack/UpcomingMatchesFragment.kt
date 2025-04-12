package com.basic.scoretrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingMatchesFragment : Fragment() {

    private var sportType: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var matchAdapter: MatchAdapter
    private var matchList: List<Cricket_UpComing> = listOf()

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

        recyclerView = view.findViewById(R.id.matchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchUpcomingMatches()
    }

    private fun fetchUpcomingMatches() {
        RetrofitClient.instance.getUpcomingMatches().enqueue(object : Callback<List<Cricket_UpComing>> {
            override fun onResponse(
                call: Call<List<Cricket_UpComing>>,
                response: Response<List<Cricket_UpComing>>
            ) {
                if (response.isSuccessful) {
                    matchList = response.body() ?: listOf()
                    Log.d("UpcomingMatches", "Matches received: ${matchList.size}")

                    matchAdapter = MatchAdapter(matchList) { match ->
                        openMatchDetails(match)
                    }
                    recyclerView.adapter = matchAdapter
                } else {
                    Toast.makeText(requireContext(), "Failed to load matches", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cricket_UpComing>>, t: Throwable) {
                Log.e("RetrofitError", "Network call failed", t)
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openMatchDetails(match: Cricket_UpComing) {
        Log.d("upcoming mathc","match details ${match}")
        val team1Logo = match.team1Logo ?: R.drawable.profile  // Replace with a default logo resource
        val team2Logo = match.team2Logo ?: R.drawable.profile  // Replace with a default logo resource
        val fragment = UpcomingMatchDetailFragment.newInstance(
            match.id,
            match.stadium,
            match.date,
            match.time,
            match.team1,
            team1Logo.toString(),
            match.team2,
            team2Logo.toString(),
            sportType ?: "Cricket"
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        fun newInstance(sportType: String) = UpcomingMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }
}
