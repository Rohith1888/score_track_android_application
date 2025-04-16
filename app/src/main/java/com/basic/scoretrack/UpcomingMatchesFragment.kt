package com.basic.scoretrack

import android.os.Bundle
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

class UpcomingMatchesFragment : Fragment() {

    private var sportType: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var matchAdapter: MatchAdapter
    private lateinit var noMatchesText: TextView
    private var matchList: List<Any> = listOf()  // Using a general type to handle both Cricket and Kabaddi
    private lateinit var loadingIndicator: View

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
        loadingIndicator = view.findViewById(R.id.loadingIndicator)

        recyclerView = view.findViewById(R.id.matchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        noMatchesText = view.findViewById(R.id.noMatchesText)
        fetchUpcomingMatches()
    }

    private fun fetchUpcomingMatches() {
        if (sportType == "Cricket") {
            fetchCricketMatches()
        } else if (sportType == "Kabaddi") {
            fetchKabaddiMatches()
        }
    }

    private fun fetchCricketMatches() {
        showLoading()
        RetrofitClient.instance.getUpcomingCricketMatches().enqueue(object : Callback<List<Cricket_UpComing>> {
            override fun onResponse(
                call: Call<List<Cricket_UpComing>>,
                response: Response<List<Cricket_UpComing>>
            ) {
                hideLoading()
                if (response.isSuccessful) {
                    matchList = response.body() ?: listOf()
                    Log.d("UpcomingMatches", "Cricket Matches received: ${matchList.size}")

                    if(matchList.isEmpty()){
                        noMatchesText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }else{
                        noMatchesText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                    matchAdapter = MatchAdapter(matchList) { match ->
                        openMatchDetails(match)
                    }
                    recyclerView.adapter = matchAdapter
                } }else {
                    Toast.makeText(requireContext(), "Failed to load Cricket matches", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Cricket_UpComing>>, t: Throwable) {
                Log.e("RetrofitError", "Network call failed: ${t.message}", t)
                hideLoading()
                when (t) {
                    is java.net.UnknownHostException -> {
                        Log.e("RetrofitError", "No internet or bad URL")
                    }
                    is java.net.SocketTimeoutException -> {
                        Log.e("RetrofitError", "Connection timed out")
                    }
                    is javax.net.ssl.SSLHandshakeException -> {
                        Log.e("RetrofitError", "SSL handshake failed")
                    }
                    else -> {
                        Log.e("RetrofitError", "Other error: ${t.localizedMessage}")
                    }
                }

                Toast.makeText(requireContext(), "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun fetchKabaddiMatches() {
        showLoading()
        RetrofitClient.instance.getUpcomingKabaddiMatches().enqueue(object : Callback<List<Kabaddi_UpComing>> {
            override fun onResponse(
                call: Call<List<Kabaddi_UpComing>>,
                response: Response<List<Kabaddi_UpComing>>
            ) {
                hideLoading()
                if (response.isSuccessful) {
                    matchList = response.body() ?: listOf()
                    Log.d("UpcomingMatches", "Kabaddi Matches received: ${matchList.size}")
                    if (matchList.isEmpty()) {
                        noMatchesText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        noMatchesText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE

                        matchAdapter = MatchAdapter(matchList) { match ->
                            openMatchDetails(match)
                        }
                        recyclerView.adapter = matchAdapter
                    }
                }else {
                    Toast.makeText(requireContext(), "Failed to load Kabaddi matches", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Kabaddi_UpComing>>, t: Throwable) {
                Log.e("RetrofitError", "Network call failed", t)
                hideLoading()
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openMatchDetails(match: Any) {
        Log.d("UpcomingMatch", "Match details: $match")

        val team1Logo = if (match is Cricket_UpComing) {
            match.team1Logo ?: R.drawable.profile
        } else if (match is Kabaddi_UpComing) {
            match.team1Logo ?: R.drawable.profile
        } else {
            R.drawable.profile
        }

        val team2Logo = if (match is Cricket_UpComing) {
            match.team2Logo ?: R.drawable.profile
        } else if (match is Kabaddi_UpComing) {
            match.team2Logo ?: R.drawable.profile
        } else {
            R.drawable.profile
        }

        val fragment = if (match is Cricket_UpComing) {
            UpcomingMatchDetailFragment.newInstance(
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
        } else if (match is Kabaddi_UpComing) {
            UpcomingMatchDetailFragment.newInstance(
                match.id,
                match.stadium,
                match.date,
                match.time,
                match.team1,
                team1Logo.toString(),
                match.team2,
                team2Logo.toString(),
                sportType ?: "Kabaddi"
            )
        } else {
            null
        }

        fragment?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentLoader, it)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance(sportType: String) = UpcomingMatchesFragment().apply {
            arguments = Bundle().apply {
                putString("SPORT_TYPE", sportType)
            }
        }
    }
    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        noMatchesText.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingIndicator.visibility = View.GONE
    }

}
