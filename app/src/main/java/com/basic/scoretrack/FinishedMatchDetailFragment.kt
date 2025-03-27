package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
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

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val teams = getPlayersForSport(sportType)
        val pagerAdapter = ViewPagerAdapterFinished(this, teams.first, teams.second)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = if (position == 0)  team1 else team2
        }.attach()
    }

    private fun getPlayersForSport(sport: String?): Pair<List<BattingStats>, List<BattingStats>> {
        return if ("Cricket" == sport) Pair(cricketBattingStats, cricketBowlingStats) else Pair(
            kabaddiTeam1Stats,
            kabaddiTeam2Stats
        )
    }

    private val cricketBattingStats = listOf(
        BattingStats("Aiden Markram", 15, 13, 1, 1, 115.38),
        BattingStats("Mitchell Marsh", 72, 36, 6, 6, 200.00),
        BattingStats("Nicholas Pooran", 75, 30, 6, 7, 250.00),
        BattingStats("David Miller", 27, 19, 1, 2, 142.11)
    )

    private val cricketBowlingStats = listOf(
        BattingStats("Jasprit Bumrah", 2, 18, 0, 0, 0.00),
        BattingStats("Rashid Khan", 3, 24, 0, 0, 0.00),
        BattingStats("Trent Boult", 1, 12, 0, 0, 0.00),
        BattingStats("Pat Cummins", 4, 28, 0, 0, 0.00)
    )

    private val kabaddiTeam1Stats = listOf(
        BattingStats("Pawan Sehrawat", 12, 10, 0, 0, 120.00),
        BattingStats("Manjeet Chhillar", 8, 9, 0, 0, 88.89),
        BattingStats("Surender Nada", 4, 6, 0, 0, 66.67),
        BattingStats("Ajay Thakur", 10, 7, 0, 0, 142.86)
    )

    private val kabaddiTeam2Stats = listOf(
        BattingStats("Rahul Chaudhari", 14, 11, 0, 0, 127.27),
        BattingStats("Deepak Hooda", 10, 9, 0, 0, 111.11),
        BattingStats("Fazel Atrachali", 5, 7, 0, 0, 71.43),
        BattingStats("Nitin Tomar", 11, 8, 0, 0, 137.50)
    )
    companion object {
        fun newInstance(
            stadium: String,
            date: String,
            result: String,
            team1: String,
            team2: String,
            sportType: String,
            team1Logo: Int,
            team2Logo: Int,
            team1Score: String,
            team2Score: String
        ): FinishedMatchDetailFragment {
            return FinishedMatchDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("STADIUM", stadium)
                    putString("DATE", date)
                    putString("RESULT", result)
                    putString("TEAM1", team1)
                    putString("TEAM2", team2)
                    putString("SPORT_TYPE", sportType)
                    putInt("TEAM1_LOGO", team1Logo)
                    putInt("TEAM2_LOGO", team2Logo)
                    putString("TEAM1_SCORE", team1Score)
                    putString("TEAM2_SCORE", team2Score)
                }
            }
        }
    }

}

class ViewPagerAdapterFinished(fragment: Fragment, private val team1Players: List<BattingStats>, private val team2Players: List<BattingStats>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return PlayerListFragmentFinished(if (position == 0) team1Players else team2Players)
    }
}

// Data class for Batting Stats
data class BattingStats(
    val playerName: String,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val strikeRate: Double
)

class PlayerListFragmentFinished(private val players: List<BattingStats>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = BattingAdapter(players)
    }
}
class BattingAdapter(private val battingList: List<BattingStats>) :
    RecyclerView.Adapter<BattingAdapter.BattingViewHolder>() {

    inner class BattingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerRuns: TextView = view.findViewById(R.id.playerRuns)
        val playerBalls: TextView = view.findViewById(R.id.playerBalls)
        val playerFours: TextView = view.findViewById(R.id.playerFours)
        val playerSixes: TextView = view.findViewById(R.id.playerSixes)
        val playerStrikeRate: TextView = view.findViewById(R.id.playerStrikeRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batting_score, parent, false)
        return BattingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BattingViewHolder, position: Int) {
        val player = battingList[position]
        holder.playerName.text = player.playerName
        holder.playerRuns.text = player.runs.toString()
        holder.playerBalls.text = player.balls.toString()
        holder.playerFours.text = player.fours.toString()
        holder.playerSixes.text = player.sixes.toString()
        holder.playerStrikeRate.text = String.format("%.2f", player.strikeRate)
    }

    override fun getItemCount() = battingList.size
}



