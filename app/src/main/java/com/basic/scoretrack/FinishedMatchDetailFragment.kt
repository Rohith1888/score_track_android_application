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

        val stadiumName: TextView = view.findViewById<TextView>(R.id.matchStadium)
        val dateText: TextView = view.findViewById<TextView>(R.id.matchDate)
        val resultText: TextView = view.findViewById<TextView>(R.id.matchResult)
        val team1Image: ImageView = view.findViewById<ImageView>(R.id.team1Logo)
        val team2Image: ImageView = view.findViewById<ImageView>(R.id.team2Logo)
        val team1ScoreText: TextView = view.findViewById<TextView>(R.id.team1Score)
        val team2ScoreText: TextView = view.findViewById<TextView>(R.id.team1Score)
        val team1Name: TextView = view.findViewById<TextView>(R.id.team1Name)
        val team2Name: TextView = view.findViewById<TextView>(R.id.team2Name)
        stadiumName.text = stadium
        dateText.text = date
        resultText.text = result
        team1Image.setImageResource(team1Logo)
        team2Image.setImageResource(team2Logo)
        team1ScoreText.text = team1Score
        team2ScoreText.text = team2Score
        team1Name.text = team1
        team2Name.text = team2

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val teams = getPlayersForSport(sportType)
        val pagerAdapter = ViewPagerAdapterFinished(this, teams.first.first, teams.first.second, teams.second.first, teams.second.second)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = if (position == 0) team1 else team2
        }.attach()
    }

    private fun getPlayersForSport(sport: String?): Pair<Pair<List<BattingStats>, List<BowlingStats>>, Pair<List<BattingStats>, List<BowlingStats>>> {
        return if ("Cricket" == sport) {
            Pair(Pair(cricketTeam1BattingStats, cricketTeam1BowlingStats), Pair(cricketTeam2BattingStats, cricketTeam2BowlingStats))
        } else {
            Pair(Pair(cricketTeam1BattingStats, cricketTeam1BowlingStats), Pair(cricketTeam2BattingStats, cricketTeam2BowlingStats))
        }
    }


    // Dummy data for Team 1 - Cricket
    private val cricketTeam1BattingStats = listOf(
        BattingStats("Virat Kohli", 68, 45, 7, 2, 151.11),
        BattingStats("Rohit Sharma", 102, 85, 9, 4, 120.00),
        BattingStats("Shikhar Dhawan", 33, 28, 4, 1, 117.85),
        BattingStats("Hardik Pandya", 50, 32, 3, 3, 156.25)
    )

    private val cricketTeam1BowlingStats = listOf(
        BowlingStats("Jasprit Bumrah", 4.0, 1, 35, 2, 8.75),
        BowlingStats("Mohammad Shami", 4.0, 0, 45, 1, 11.25),
        BowlingStats("Ravindra Jadeja", 4.0, 1, 30, 3, 7.50),
        BowlingStats("Yuzvendra Chahal", 4.0, 0, 36, 0, 9.00)
    )

    // Dummy data for Team 2 - Cricket
    private val cricketTeam2BattingStats = listOf(
        BattingStats("David Warner", 45, 30, 4, 2, 150.00),
        BattingStats("Steve Smith", 67, 55, 5, 2, 121.82),
        BattingStats("Glenn Maxwell", 36, 22, 3, 1, 163.64),
        BattingStats("Matthew Wade", 52, 38, 4, 3, 136.84)
    )

    private val cricketTeam2BowlingStats = listOf(
        BowlingStats("Pat Cummins", 4.0, 0, 33, 2, 8.25),
        BowlingStats("Mitchell Starc", 4.0, 1, 41, 1, 10.25),
        BowlingStats("Adam Zampa", 4.0, 0, 28, 3, 7.00),
        BowlingStats("Kane Richardson", 4.0, 0, 38, 0, 9.50)
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

class ViewPagerAdapterFinished(
    fragment: Fragment,
    private val team1Batting: List<BattingStats>,
    private val team1Bowling: List<BowlingStats>,
    private val team2Batting: List<BattingStats>,
    private val team2Bowling: List<BowlingStats>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            PlayerListFragmentFinished(team1Batting, team1Bowling)
        else
            PlayerListFragmentFinished(team2Batting, team2Bowling)
    }
}

data class BattingStats(
    val playerName: String,
    val runs: Int,
    val balls: Int,
    val fours: Int,
    val sixes: Int,
    val strikeRate: Double
)

data class BowlingStats(
    val playerName: String,
    val overs: Double,
    val maidens: Int,
    val runs: Int,
    val wickets: Int,
    val economy: Double
)

class PlayerListFragmentFinished(
    private val battingList: List<BattingStats>,
    private val bowlingList: List<BowlingStats>
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_list_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val battingRecyclerView = view.findViewById<RecyclerView>(R.id.battingRecyclerView)
        val bowlingRecyclerView = view.findViewById<RecyclerView>(R.id.bowlingRecyclerView)

        battingRecyclerView.layoutManager = LinearLayoutManager(context)
        bowlingRecyclerView.layoutManager = LinearLayoutManager(context)

        battingRecyclerView.adapter = BattingAdapter(battingList)
        bowlingRecyclerView.adapter = BowlingAdapter(bowlingList)
    }
}

class BowlingAdapter(private val bowlingList: List<BowlingStats>) :
    RecyclerView.Adapter<BowlingAdapter.BowlingViewHolder>() {

    inner class BowlingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val overs: TextView = view.findViewById(R.id.overs)
        val maidens: TextView = view.findViewById(R.id.maidens)
        val runs: TextView = view.findViewById(R.id.runs)
        val wickets: TextView = view.findViewById(R.id.wickets)
        val economy: TextView = view.findViewById(R.id.economy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BowlingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bowling_score, parent, false)
        return BowlingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BowlingViewHolder, position: Int) {
        val player = bowlingList[position]
        holder.playerName.text = player.playerName
        holder.overs.text = player.overs.toString()
        holder.maidens.text = player.maidens.toString()
        holder.runs.text = player.runs.toString()
        holder.wickets.text = player.wickets.toString()
        holder.economy.text = String.format("%.2f", player.economy)
    }

    override fun getItemCount() = bowlingList.size
}

class BattingAdapter(private val battingList: List<BattingStats>) :
    RecyclerView.Adapter<BattingAdapter.BattingViewHolder>() {

    inner class BattingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerRuns: TextView = view.findViewById(R.id.runs)
        val playerBalls: TextView = view.findViewById(R.id.balls)
        val playerFours: TextView = view.findViewById(R.id.fours)
        val playerSixes: TextView = view.findViewById(R.id.sixes)
        val playerStrikeRate: TextView = view.findViewById(R.id.strikeRate)
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
