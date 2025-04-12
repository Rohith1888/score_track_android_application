package com.basic.scoretrack

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpcomingMatchDetailFragment : Fragment() {

    private var matchId: Int? = null
    private var stadium: String? = null
    private var date: String? = null
    private var time: String? = null
    private var team1: String? = null
    private var team1Logo: String? = null
    private var team2: String? = null
    private var team2Logo: String? = null
    private var sportType: String? = null

    private lateinit var team1Players: List<PlayerResponseUpcoming>
    private lateinit var team2Players: List<PlayerResponseUpcoming>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            matchId = it.getInt("MATCH_ID")
            stadium = it.getString("STADIUM")
            date = it.getString("DATE")
            time = it.getString("TIME")
            team1 = it.getString("TEAM1")
            team1Logo = it.getString("TEAM1_LOGO")
            team2 = it.getString("TEAM2")
            team2Logo = it.getString("TEAM2_LOGO")
            sportType = it.getString("SPORT_TYPE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_upcoming_match_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stadiumText: TextView = view.findViewById(R.id.matchStadium)
        val matchDate: TextView = view.findViewById(R.id.matchDate)
        val matchTime: TextView = view.findViewById(R.id.matchTime)
        val team1LogoView: ImageView = view.findViewById(R.id.team1Logo)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team2LogoView: ImageView = view.findViewById(R.id.team2Logo)
        val team2Name: TextView = view.findViewById(R.id.team2Name)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        Log.d("MatchDetails", "Match ID: $matchId")
        stadiumText.text = stadium
        matchDate.text = date
        matchTime.text = "Starts at: $time"
        team1Name.text = team1
        team2Name.text = team2
        context?.let {
            Glide.with(it)
                .load(team1Logo)
                .placeholder(R.drawable.profile_image)
                .into(team1LogoView)

            Glide.with(it)
                .load(team2Logo)
                .placeholder(R.drawable.profile_image)
                .into(team2LogoView)
        }

        matchId?.let { id ->
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val matchDetails = RetrofitClient.instance.getMatchDetails(id)

                    // ✅ Log full matchDetails response
                    Log.d("MatchDetails", "MatchDetails response: $matchDetails")

                    team1Players = matchDetails.team1Players
                    team2Players = matchDetails.team2Players

                    // Also log team players
                    Log.d("MatchDetails", "Team1Players: $team1Players")
                    Log.d("MatchDetails", "Team2Players: $team2Players")

                    updateUIWithMatchDetails(matchDetails)

                } catch (e: Exception) {
                    Log.d("MatchDetails", "Fetching match details for ID: $id")

                    Log.e("MatchDetails", "Error fetching match details", e)
                    Toast.makeText(requireContext(), "Failed to load match details", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun updateUIWithMatchDetails(matchDetails: MatchDetailsResponse) {
        stadium = matchDetails.stadium
        date = matchDetails.date
        time = matchDetails.time
        team1 = matchDetails.team1
        team1Logo = matchDetails.team1Logo
        team2 = matchDetails.team2
        team2Logo = matchDetails.team2Logo
        sportType = matchDetails.sportType


        view?.findViewById<TextView>(R.id.matchStadium)?.text = stadium
        view?.findViewById<TextView>(R.id.matchDate)?.text = date
        view?.findViewById<TextView>(R.id.matchTime)?.text = "Starts at: $time"
        view?.findViewById<TextView>(R.id.team1Name)?.text = team1
        view?.findViewById<TextView>(R.id.team2Name)?.text = team2

        context?.let {
            view?.findViewById<ImageView>(R.id.team1Logo)?.let { team1LogoView ->
                Glide.with(it)
                    .load(team1Logo)
                    .placeholder(R.drawable.profile_image)
                    .into(team1LogoView)
            }

            view?.findViewById<ImageView>(R.id.team2Logo)?.let { team2LogoView ->
                Glide.with(it)
                    .load(team2Logo)
                    .placeholder(R.drawable.profile_image)
                    .into(team2LogoView)
            }
        }

        // Set up the ViewPager with player data
        val pagerAdapter = ViewPagerAdapter(this, team1Players, team2Players)
        view?.findViewById<ViewPager2>(R.id.viewPager)?.adapter = pagerAdapter

        TabLayoutMediator(view?.findViewById(R.id.tabLayout)!!, view?.findViewById(R.id.viewPager)!!) { tab, position ->
            tab.text = if (position == 0) team1 else team2
        }.attach()

    }

    companion object {
        fun newInstance(
            matchId: Int,
            stadium: String,
            date: String,
            time: String,
            team1: String,
            team1Logo: String,
            team2: String,
            team2Logo: String,
            sportType: String
        ) = UpcomingMatchDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("MATCH_ID", matchId)
                putString("STADIUM", stadium)
                putString("DATE", date)
                putString("TIME", time)
                putString("TEAM1", team1)
                putString("TEAM1_LOGO", team1Logo)
                putString("TEAM2", team2)
                putString("TEAM2_LOGO", team2Logo)
                putString("SPORT_TYPE", sportType)
            }
        }
    }
}

