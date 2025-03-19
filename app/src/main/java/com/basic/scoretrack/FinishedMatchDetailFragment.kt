package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FinishedMatchDetailFragment : Fragment() {
    private var stadium: String? = null
    private var date: String? = null
    private var result: String? = null
    private var team1: String? = null
    private var team2: String? = null
    private var sportType: String? = null
    private var team1Logo = 0
    private var team2Logo = 0
    private var team1Score: String? = null
    private var team2Score: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            stadium = requireArguments().getString("STADIUM")
            date = requireArguments().getString("DATE")
            result = requireArguments().getString("RESULT")
            team1 = requireArguments().getString("TEAM1")
            team1Logo = requireArguments().getInt("TEAM1_LOGO")
            team2 = requireArguments().getString("TEAM2")
            team2Logo = requireArguments().getInt("TEAM2_LOGO")
            sportType = requireArguments().getString("SPORT_TYPE")
            team1Score = requireArguments().getString("TEAM1_SCORE")
            team2Score = requireArguments().getString("TEAM2_SCORE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finished_match_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stadiumText = view.findViewById<TextView>(R.id.matchStadium)
        val matchDate = view.findViewById<TextView>(R.id.matchDate)
        val matchResult = view.findViewById<TextView>(R.id.matchResult)
        val team1LogoView = view.findViewById<ImageView>(R.id.team1Logo)
        val team1Name = view.findViewById<TextView>(R.id.team1Name)
        val team1ScoreView = view.findViewById<TextView>(R.id.team1Score)
        val team2LogoView = view.findViewById<ImageView>(R.id.team2Logo)
        val team2Name = view.findViewById<TextView>(R.id.team2Name)
        val team2ScoreView = view.findViewById<TextView>(R.id.team2Score)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        stadiumText.text = stadium
        matchDate.text = date
        matchResult.text = result
        team1Name.text = team1
        team1ScoreView.text = team1Score
        team2Name.text = team2
        team2ScoreView.text = team2Score

        team1LogoView.setImageResource(team1Logo)
        team2LogoView.setImageResource(team2Logo)

        val teams = getPlayersForSport(sportType)
        val pagerAdapter = ViewPagerAdapter(this, teams.first, teams.second)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setText(if (position == 0) team1 else team2)
        }.attach()
    }

    private fun getPlayersForSport(sport: String?): Pair<List<Player>, List<Player>> {
        return if ("Cricket" == sport) Pair(cricketTeam1Players, cricketTeam2Players) else Pair(
            kabaddiTeam1Players,
            kabaddiTeam2Players
        )
    }
    companion object {
        fun newInstance(
            stadium: String, date: String, result: String,
            team1: String, team2: String, sportType: String,
            team1Logo: Int, team2Logo: Int,
            team1Score: String, team2Score: String
        ) = FinishedMatchDetailFragment().apply {
            arguments = Bundle().apply {
                putString("STADIUM", stadium)
                putString("DATE", date)
                putString("RESULT", result)  // Added missing result parameter
                putString("TEAM1", team1)
                putInt("TEAM1_LOGO", team1Logo)
                putString("TEAM1_SCORE", team1Score)  // Added missing team1Score
                putString("TEAM2", team2)
                putInt("TEAM2_LOGO", team2Logo)
                putString("TEAM2_SCORE", team2Score)  // Added missing team2Score
                putString("SPORT_TYPE", sportType)  // Passing the sport type
            }
        }

    }

        private val cricketTeam1Players = listOf(
            Player("Shreyas Iyer", "Right-hand batsman"),
            Player("Sunil Narine", "Left-hand batsman, Off-spin bowler"),
            Player("Andre Russell", "Right-hand batsman, Fast bowler"),
            Player("Rinku Singh", "Left-hand batsman"),
            Player("Varun Chakravarthy", "Right-arm leg spin bowler"),
            Player("Mitchell Starc", "Left-arm fast bowler"),
            Player("Venkatesh Iyer", "All-rounder"),
            Player("Rahmanullah Gurbaz", "Wicketkeeper, Right-hand batsman"),
            Player("Nitish Rana", "Left-hand batsman, Off-spin bowler"),
            Player("Lockie Ferguson", "Right-arm fast bowler"),
            Player("Harshit Rana", "Right-arm fast bowler")
        )

        private val cricketTeam2Players = listOf(
            Player("Virat Kohli", "Right-hand batsman"),
            Player("Faf du Plessis", "Right-hand batsman"),
            Player("Glenn Maxwell", "All-rounder"),
            Player("Dinesh Karthik", "Wicketkeeper, Right-hand batsman"),
            Player("Mohammed Siraj", "Right-arm fast bowler"),
            Player("Harshal Patel", "Right-arm medium-fast bowler"),
            Player("Cameron Green", "All-rounder"),
            Player("Rajat Patidar", "Right-hand batsman"),
            Player("Josh Hazlewood", "Right-arm fast bowler"),
            Player("Wanindu Hasaranga", "Leg-spin bowler, All-rounder"),
            Player("Karn Sharma", "Leg-spin bowler")
        )

        private val kabaddiTeam1Players = listOf(
            Player("Pawan Sehrawat", "Raider"),
            Player("Manjeet Chhillar", "All-rounder"),
            Player("Surender Nada", "Defender - Left Corner"),
            Player("Ajay Thakur", "Raider"),
            Player("Sandeep Narwal", "All-rounder"),
            Player("Parvesh Bhainswal", "Defender - Left Cover"),
            Player("Vishal Bhardwaj", "Defender - Left Corner"),
            Player("Naveen Kumar", "Raider"),
            Player("Mahender Singh", "Defender - Right Cover"),
            Player("Mohit Chhillar", "Defender - Right Corner"),
            Player("Rohit Kumar", "Raider")
        )

        private val kabaddiTeam2Players = listOf(
            Player("Fazel Atrachali", "Defender - Left Corner"),
            Player("Deepak Hooda", "All-rounder"),
            Player("Rahul Chaudhari", "Raider"),
            Player("Nitin Tomar", "Raider"),
            Player("Girish Ernak", "Defender - Left Corner"),
            Player("Siddharth Desai", "Raider"),
            Player("Ravinder Pahal", "Defender - Right Corner"),
            Player("Abhishek Singh", "Raider"),
            Player("Neeraj Kumar", "Defender - Right Cover"),
            Player("Sunil Kumar", "Defender - Left Cover"),
            Player("Pradeep Narwal", "Raider")
        )
    }

