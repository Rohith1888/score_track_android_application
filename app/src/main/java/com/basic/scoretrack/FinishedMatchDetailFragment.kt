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
        arguments?.let {
            stadium = it.getString("STADIUM")
            date = it.getString("DATE")
            result = it.getString("RESULT")
            team1 = it.getString("TEAM1")
            team2 = it.getString("TEAM2")
            sportType = it.getString("SPORT_TYPE")
            team1Logo = it.getInt("TEAM1_LOGO")
            team2Logo = it.getInt("TEAM2_LOGO")
            team1Score = it.getString("TEAM1_SCORE")
            team2Score = it.getString("TEAM2_SCORE")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finished_match_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stadiumName: TextView = view.findViewById(R.id.matchStadium)
        val dateText: TextView = view.findViewById(R.id.matchDate)
        val resultText: TextView = view.findViewById(R.id.matchResult)
        val team1Image: ImageView = view.findViewById(R.id.team1Logo)
        val team2Image: ImageView = view.findViewById(R.id.team2Logo)
        val team1ScoreText: TextView = view.findViewById(R.id.team1Score)
        val team2ScoreText: TextView = view.findViewById(R.id.team2Score)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team2Name: TextView = view.findViewById(R.id.team2Name)

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

        val pagerAdapter = ViewPagerAdapterFinished(
            this,
            sportType = sportType ?: "Cricket",
            team1Batting = teams.first.first,
            team1Bowling = teams.first.second,
            team2Batting = teams.second.first,
            team2Bowling = teams.second.second,
            team1Kabaddi = teams.third.first,
            team2Kabaddi = teams.third.second
        )

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) team1 else team2
        }.attach()
    }

    // Modified to return a Triple for Kabaddi too
    private fun getPlayersForSport(sport: String?): Triple<Pair<List<BattingStats>, List<BowlingStats>>, Pair<List<BattingStats>, List<BowlingStats>>, Pair<List<KabaddiStats>, List<KabaddiStats>>> {
        return if (sport == "Cricket") {
            Triple(
                Pair(cricketTeam1BattingStats, cricketTeam1BowlingStats),
                Pair(cricketTeam2BattingStats, cricketTeam2BowlingStats),
                Pair(emptyList(), emptyList()) // No kabaddi data
            )
        } else {
            Triple(
                Pair(emptyList(), emptyList()),
                Pair(emptyList(), emptyList()),
                Pair(kabaddiTeam1Stats, kabaddiTeam2Stats)
            )
        }
    }

    // Dummy Cricket Data (same as before)
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

    // Dummy Kabaddi Data
    private val kabaddiTeam1Stats = listOf(
        KabaddiStats("Pardeep Narwal", 10, 2, 0),
        KabaddiStats("Naveen Kumar", 8, 1, 1),
        KabaddiStats("Surender Gill", 7, 0, 1),
        KabaddiStats("Sandeep Narwal", 4, 4, 0)
    )
    private val kabaddiTeam2Stats = listOf(
        KabaddiStats("Fazel Atrachali", 2, 6, 0),
        KabaddiStats("Maninder Singh", 9, 1, 0),
        KabaddiStats("Rahul Chaudhari", 6, 0, 1),
        KabaddiStats("Neeraj Kumar", 3, 5, 1)
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
    private val sportType: String,
    private val team1Batting: List<BattingStats> = emptyList(),
    private val team1Bowling: List<BowlingStats> = emptyList(),
    private val team2Batting: List<BattingStats> = emptyList(),
    private val team2Bowling: List<BowlingStats> = emptyList(),
    private val team1Kabaddi: List<KabaddiStats> = emptyList(),
    private val team2Kabaddi: List<KabaddiStats> = emptyList()
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (sportType == "Cricket") {
            if (position == 0)
                PlayerListFragmentFinished(team1Batting, team1Bowling, emptyList(), "Cricket")
            else
                PlayerListFragmentFinished(team2Batting, team2Bowling, emptyList(), "Cricket")
        } else {
            if (position == 0)
                PlayerListFragmentFinished(emptyList(), emptyList(), team1Kabaddi, "Kabaddi")
            else
                PlayerListFragmentFinished(emptyList(), emptyList(), team2Kabaddi, "Kabaddi")
        }
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
    private val bowlingList: List<BowlingStats>,
    private val kabaddiList: List<KabaddiStats> = emptyList(),
    private val sportType: String
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return if (sportType == "Cricket") {
            inflater.inflate(R.layout.fragment_player_list_finished, container, false)
        } else {
            inflater.inflate(R.layout.fragment_player_list_kabaddi, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sportType == "Cricket") {
            val battingRecyclerView = view.findViewById<RecyclerView>(R.id.battingRecyclerView)
            val bowlingRecyclerView = view.findViewById<RecyclerView>(R.id.bowlingRecyclerView)

            battingRecyclerView.layoutManager = LinearLayoutManager(context)
            bowlingRecyclerView.layoutManager = LinearLayoutManager(context)

            battingRecyclerView.adapter = BattingAdapter(battingList)
            bowlingRecyclerView.adapter = BowlingAdapter(bowlingList)
        } else {
            val kabaddiRecyclerView = view.findViewById<RecyclerView>(R.id.kabaddiRecyclerView)
            kabaddiRecyclerView.layoutManager = LinearLayoutManager(context)
            kabaddiRecyclerView.adapter = KabaddiAdapter(kabaddiList)
        }
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
data class KabaddiStats(
    val playerName: String,
    val raidPoints: Int,
    val tacklePoints: Int,
    val totalPoints: Int
)

class KabaddiAdapter(private val playerList: List<KabaddiStats>) :
    RecyclerView.Adapter<KabaddiAdapter.KabaddiViewHolder>() {

    inner class KabaddiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val raidPoints: TextView = view.findViewById(R.id.raidPoints)
        val tacklePoints: TextView = view.findViewById(R.id.tacklePoints)
        val totalPoints: TextView = view.findViewById(R.id.totalPoints)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KabaddiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kabaddi_score, parent, false)
        return KabaddiViewHolder(view)
    }

    override fun onBindViewHolder(holder: KabaddiViewHolder, position: Int) {
        val player = playerList[position]
        holder.playerName.text = player.playerName
        holder.raidPoints.text = player.raidPoints.toString()
        holder.tacklePoints.text = player.tacklePoints.toString()
        holder.totalPoints.text = player.totalPoints.toString()
    }

    override fun getItemCount(): Int = playerList.size
}
