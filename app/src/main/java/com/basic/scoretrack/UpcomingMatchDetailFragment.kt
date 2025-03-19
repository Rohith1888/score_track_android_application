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

class UpcomingMatchDetailFragment : Fragment() {

    private var stadium: String? = null
    private var date: String? = null
    private var time: String? = null
    private var team1: String? = null
    private var team1Logo: Int? = null
    private var team2: String? = null
    private var team2Logo: Int? = null
    private var sportType: String? = null  // Added to differentiate Cricket and Kabaddi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stadium = it.getString("STADIUM")
            date = it.getString("DATE")
            time = it.getString("TIME")
            team1 = it.getString("TEAM1")
            team1Logo = it.getInt("TEAM1_LOGO")
            team2 = it.getString("TEAM2")
            team2Logo = it.getInt("TEAM2_LOGO")
            sportType = it.getString("SPORT_TYPE")  // Retrieve sport type
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

        stadiumText.text = stadium
        matchDate.text = date
        matchTime.text = "Starts at: $time"
        team1Name.text = team1
        team2Name.text = team2

        team1Logo?.let { team1LogoView.setImageResource(it) }
        team2Logo?.let { team2LogoView.setImageResource(it) }

        val (team1Players, team2Players) = getPlayersForSport(sportType)

        val pagerAdapter = ViewPagerAdapter(this, team1Players, team2Players)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) team1 else team2
        }.attach()
    }

    // Function to return player lists dynamically based on the sport type
    private fun getPlayersForSport(sport: String?): Pair<List<Player>, List<Player>> {
        return if (sport == "Cricket") {
            cricketTeam1Players to cricketTeam2Players
        } else {
            kabaddiTeam1Players to kabaddiTeam2Players
        }
    }

    companion object {
        fun newInstance(
            stadium: String, date: String, time: String,
            team1: String, team1Logo: Int, team2: String, team2Logo: Int,
            sportType: String // New argument for identifying the sport
        ) = UpcomingMatchDetailFragment().apply {
            arguments = Bundle().apply {
                putString("STADIUM", stadium)
                putString("DATE", date)
                putString("TIME", time)
                putString("TEAM1", team1)
                putInt("TEAM1_LOGO", team1Logo)
                putString("TEAM2", team2)
                putInt("TEAM2_LOGO", team2Logo)
                putString("SPORT_TYPE", sportType)  // Passing the sport type
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
}
