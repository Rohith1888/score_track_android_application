package com.basic.scoretrack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveMatchesFragment : Fragment() {

    private var sportType: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var liveMatchAdapter: LiveMatchAdapter
    private lateinit var noMatchesText: TextView
    private lateinit var loadingIndicator: View
    private var matchList: List<LiveCricketMatch> = listOf()

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var fetchRunnable: Runnable
    private val fetchInterval: Long = 15000L // 15 seconds

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
        return inflater.inflate(R.layout.fragment_live_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.liveMatchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        noMatchesText = view.findViewById(R.id.noLiveMatchesText)
        loadingIndicator = view.findViewById(R.id.loadingLiveIndicator)

        val title = view.findViewById<TextView>(R.id.sportTitle)
        title.text = "$sportType - Live Matches"

        liveMatchAdapter = LiveMatchAdapter(mutableListOf()) { match -> openLiveMatchDetails(match) }
        recyclerView.adapter = liveMatchAdapter

        if (sportType.equals("Kabaddi", ignoreCase = true)) {
            // Show "No Matches" directly for Kabaddi
            loadingIndicator.visibility = View.GONE
            recyclerView.visibility = View.GONE
            noMatchesText.visibility = View.VISIBLE
        } else {
            startPeriodicFetch()
        }
    }

    private fun fetchLiveMatches() {
        RetrofitClient.instance.getAllLiveMatches().enqueue(object : Callback<List<LiveCricketMatch>> {
            override fun onResponse(
                call: Call<List<LiveCricketMatch>>,
                response: Response<List<LiveCricketMatch>>
            ) {
                if (response.isSuccessful) {
                    val updatedList = response.body() ?: listOf()

                    matchList = updatedList
                    liveMatchAdapter.updateData(matchList)

                    if (matchList.isEmpty()) {
                        noMatchesText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noMatchesText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load live matches", Toast.LENGTH_SHORT).show()
                }
                hideLoading()
            }

            override fun onFailure(call: Call<List<LiveCricketMatch>>, t: Throwable) {
                hideLoading()
                Log.e("LiveMatchesError", "Error fetching live matches", t)
                Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openLiveMatchDetails(match: LiveCricketMatch) {
        val team1Logo = match.team1Logo ?: R.drawable.profile
        val team2Logo = match.team2Logo ?: R.drawable.profile

        val fragment = LiveMatchDetailFragment.newInstance(
            match.id,
            match.stadium,
            match.date,
            match.team1,
            match.team2,
            sportType ?: "Cricket",
            team1Logo.toString(),
            team2Logo.toString(),
            match.team1Score,
            match.team2Score,
            match.matchDecision
        )

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentLoader, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun startPeriodicFetch() {
        showLoading()

        fetchRunnable = object : Runnable {
            override fun run() {
                fetchLiveMatches()
                handler.postDelayed(this, fetchInterval)
            }
        }
        handler.post(fetchRunnable)
    }

    private fun stopPeriodicFetch() {
        handler.removeCallbacks(fetchRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopPeriodicFetch()
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noMatchesText.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.GONE
    }

    companion object {
        fun newInstance(sportType: String) = LiveMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }
}
