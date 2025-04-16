package com.basic.scoretrack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveMatchDetailFragment : Fragment() {

    private var matchId: Int = 0
    private var sportType: String = "Cricket"

    // UI refs
    private lateinit var stadiumName: TextView
    private lateinit var dateText: TextView
    private lateinit var matchDecisionText: TextView
    private lateinit var team1Image: ImageView
    private lateinit var team2Image: ImageView
    private lateinit var team1ScoreText: TextView
    private lateinit var team2ScoreText: TextView
    private lateinit var team1Name: TextView
    private lateinit var team2Name: TextView
    private lateinit var loading: ProgressBar

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    // periodic refresh
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var refreshRunnable: Runnable
    private val refreshIntervalMs = 15000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("MATCH_ID")?.let { matchId = it }
        arguments?.getString("SPORT_TYPE")?.let { sportType = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_live_match_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind views
        stadiumName       = view.findViewById(R.id.matchStadium)
        dateText          = view.findViewById(R.id.matchDate)
        matchDecisionText = view.findViewById(R.id.matchDecision)
        team1Image        = view.findViewById(R.id.team1Logo)
        team2Image        = view.findViewById(R.id.team2Logo)
        team1ScoreText    = view.findViewById(R.id.team1Score)
        team2ScoreText    = view.findViewById(R.id.team2Score)
        team1Name         = view.findViewById(R.id.team1Name)
        team2Name         = view.findViewById(R.id.team2Name)
        loading           = view.findViewById(R.id.detailLoading)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        // initial load with spinner
        fetchMatchDetails(showLoading = true)
    }

    override fun onResume() {
        super.onResume()
        // start periodic refresh (without showing spinner)
        startAutoRefresh()
    }

    override fun onPause() {
        super.onPause()
        // stop auto refresh
        handler.removeCallbacks(refreshRunnable)
    }

    private fun startAutoRefresh() {
        refreshRunnable = Runnable {
            fetchMatchDetails(showLoading = false)
            handler.postDelayed(refreshRunnable, refreshIntervalMs)
        }
        handler.postDelayed(refreshRunnable, refreshIntervalMs)
    }

    private fun fetchMatchDetails(showLoading: Boolean) {
        if (showLoading) loading.visibility = View.VISIBLE

        RetrofitClient.instance
            .getLiveMatchById(matchId)
            .enqueue(object : Callback<LiveCricketMatch> {
                override fun onResponse(call: Call<LiveCricketMatch>, response: Response<LiveCricketMatch>) {
                    if (showLoading) loading.visibility = View.GONE

                    if (!response.isSuccessful) {
                        Toast.makeText(requireContext(), "Failed to load match", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val m = response.body()!!

                    // 1) Header info
                    stadiumName.text       = m.stadium
                    dateText.text          = m.date
                    matchDecisionText.text = m.matchDecision
                    team1ScoreText.text    = m.team1Score
                    team2ScoreText.text    = m.team2Score
                    team1Name.text         = m.team1
                    team2Name.text         = m.team2

                    // 2) Logos
                    Glide.with(this@LiveMatchDetailFragment)
                        .load(m.team1Logo)
                        .placeholder(R.drawable.profile_image)
                        .into(team1Image)

                    Glide.with(this@LiveMatchDetailFragment)
                        .load(m.team2Logo)
                        .placeholder(R.drawable.profile_image)
                        .into(team2Image)

                    // 3) Build stats lists
                    val team1Bat = m.team1BattingStats.map {
                        BattingStats(it.playerName, it.runs ?: 0, it.balls ?: 0, it.fours ?: 0, it.sixes ?: 0, it.strikeRate ?: 0.0)
                    }
                    val team1Bowl = m.team1BowlingStats.map {
                        BowlingStats(it.playerName, it.overs ?: 0.0, it.maidens ?: 0, it.runs ?: 0, it.wickets ?: 0, it.economy ?: 0.0)
                    }
                    val team2Bat = m.team2BattingStats.map {
                        BattingStats(it.playerName, it.runs ?: 0, it.balls ?: 0, it.fours ?: 0, it.sixes ?: 0, it.strikeRate ?: 0.0)
                    }
                    val team2Bowl = m.team2BowlingStats.map {
                        BowlingStats(it.playerName, it.overs ?: 0.0, it.maidens ?: 0, it.runs ?: 0, it.wickets ?: 0, it.economy ?: 0.0)
                    }

                    // 4) ViewPager + Tabs
                    val adapter = ViewPagerAdapterFinished(
                        this@LiveMatchDetailFragment,
                        sportType,
                        team1Bat, team1Bowl,
                        team2Bat, team2Bowl,
                        emptyList(), emptyList()
                    )
                    viewPager.adapter = adapter

                    TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                        tab.text = if (pos == 0) m.team1 else m.team2
                    }.attach()
                }

                override fun onFailure(call: Call<LiveCricketMatch>, t: Throwable) {
                    if (showLoading) loading.visibility = View.GONE
                    Log.e("LiveMatchDetail", "failed", t)
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {
        fun newInstance(matchId: Int, sportType: String): LiveMatchDetailFragment {
            return LiveMatchDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("MATCH_ID", matchId)
                    putString("SPORT_TYPE", sportType)
                }
            }
        }
    }
}
