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
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LiveMatchDetailFragment : Fragment() {

    private var stadium: String? = null
    private var date: String? = null
    private var team1: String? = null
    private var team2: String? = null
    private var sportType: String? = null
    private var team1Logo:String?=null
    private var team2Logo:String?=null
    private var team1Score: String? = null
    private var team2Score: String? = null
    private var matchDecision: String? = null
    private var matchId: Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            matchId = it.getInt("MATCHID")
            stadium = it.getString("STADIUM")
            date = it.getString("DATE")
            team1 = it.getString("TEAM1")
            team2 = it.getString("TEAM2")
            sportType = it.getString("SPORT_TYPE")
            team1Logo = it.getString("TEAM1_LOGO")
            team2Logo = it.getString("TEAM2_LOGO")
            team1Score = it.getString("TEAM1_SCORE")
            team2Score = it.getString("TEAM2_SCORE")
            matchDecision = it.getString("MATCH_DECISION")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_match_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stadiumName: TextView = view.findViewById(R.id.matchStadium)
        val dateText: TextView = view.findViewById(R.id.matchDate)
        val matchDecisionText: TextView = view.findViewById(R.id.matchDecision)
        val team1Image: ImageView = view.findViewById(R.id.team1Logo)
        val team2Image: ImageView = view.findViewById(R.id.team2Logo)
        val team1ScoreText: TextView = view.findViewById(R.id.team1Score)
        val team2ScoreText: TextView = view.findViewById(R.id.team2Score)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team2Name: TextView = view.findViewById(R.id.team2Name)

        stadiumName.text = stadium
        dateText.text = date
        matchDecisionText.text = matchDecision
        context?.let {
            Glide.with(it)
                .load(team1Logo)
                .placeholder(R.drawable.profile_image)
                .into(team1Image)

            Glide.with(it)
                .load(team2Logo)
                .placeholder(R.drawable.profile_image)
                .into(team2Image)
        }

        team1ScoreText.text = team1Score
        team2ScoreText.text = team2Score
        team1Name.text = team1
        team2Name.text = team2

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)

        val players = getLivePlayers()

        val pagerAdapter = ViewPagerAdapterLive(
            this,
            sportType = sportType ?: "Cricket",
            team1Batting = players.first.first,
            team1Bowling = players.first.second,
            team2Batting = players.second.first,
            team2Bowling = players.second.second,
            team1Kabaddi = players.third.first,
            team2Kabaddi = players.third.second
        )

        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) team1 else team2
        }.attach()
    }

    private fun getLivePlayers(): Triple<Pair<List<BattingStats>, List<BowlingStats>>, Pair<List<BattingStats>, List<BowlingStats>>, Pair<List<KabaddiStats>, List<KabaddiStats>>> {
        return if (sportType == "Cricket") {
            Triple(
                Pair(liveTeam1BattingStats, liveTeam1BowlingStats),
                Pair(liveTeam2BattingStats, liveTeam2BowlingStats),
                Pair(emptyList(), emptyList())
            )
        } else {
            Triple(
                Pair(emptyList(), emptyList()),
                Pair(emptyList(), emptyList()),
                Pair(liveKabaddiTeam1Stats, liveKabaddiTeam2Stats)
            )
        }
    }

    private val liveTeam1BattingStats = listOf(
        BattingStats("Ruturaj Gaikwad", 35, 28, 4, 1, 125.00),
        BattingStats("Devon Conway", 48, 34, 5, 2, 141.17)
    )
    private val liveTeam1BowlingStats = listOf(
        BowlingStats("Deepak Chahar", 3.0, 0, 21, 2, 7.00),
        BowlingStats("Maheesh Theekshana", 4.0, 0, 36, 1, 9.00)
    )
    private val liveTeam2BattingStats = listOf(
        BattingStats("Ishan Kishan", 22, 15, 3, 1, 146.67),
        BattingStats("Tilak Varma", 40, 25, 5, 1, 160.00)
    )
    private val liveTeam2BowlingStats = listOf(
        BowlingStats("Jasprit Bumrah", 4.0, 1, 19, 2, 4.75),
        BowlingStats("Piyush Chawla", 3.0, 0, 29, 0, 9.67)
    )

    private val liveKabaddiTeam1Stats = listOf(
        KabaddiStats("Pardeep Narwal", 6, 3, 9),
        KabaddiStats("Naveen Kumar", 5, 1, 6)
    )
    private val liveKabaddiTeam2Stats = listOf(
        KabaddiStats("Maninder Singh", 7, 2, 9),
        KabaddiStats("Neeraj Kumar", 2, 4, 6)
    )

    companion object {
        fun newInstance(
            matchId: Int,
            stadium: String,
            date: String,
            team1: String,
            team2: String,
            sportType: String,
            team1Logo: String,
            team2Logo: String,
            team1Score: String,
            team2Score: String,
            matchDecision: String
        ): LiveMatchDetailFragment {
            return LiveMatchDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("MATCHID", matchId)
                    putString("STADIUM", stadium)
                    putString("DATE", date)
                    putString("TEAM1", team1)
                    putString("TEAM2", team2)
                    putString("SPORT_TYPE", sportType)
                    putString("TEAM1_LOGO", team1Logo)
                    putString("TEAM2_LOGO", team2Logo)
                    putString("TEAM1_SCORE", team1Score)
                    putString("TEAM2_SCORE", team2Score)
                    putString("MATCH_DECISION", matchDecision)
                }
            }
        }
    }
}

class ViewPagerAdapterLive(
    fragment: Fragment,
    private val sportType: String,
    private val team1Batting: List<BattingStats>,
    private val team1Bowling: List<BowlingStats>,
    private val team2Batting: List<BattingStats>,
    private val team2Bowling: List<BowlingStats>,
    private val team1Kabaddi: List<KabaddiStats>,
    private val team2Kabaddi: List<KabaddiStats>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (sportType == "Cricket") {
            if (position == 0)
                PlayerListFragmentLive(team1Batting, team1Bowling, emptyList(), "Cricket")
            else
                PlayerListFragmentLive(team2Batting, team2Bowling, emptyList(), "Cricket")
        } else {
            if (position == 0)
                PlayerListFragmentLive(emptyList(), emptyList(), team1Kabaddi, "Kabaddi")
            else
                PlayerListFragmentLive(emptyList(), emptyList(), team2Kabaddi, "Kabaddi")
        }
    }
}

class PlayerListFragmentLive(
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

/*data class BattingStats(
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

data class KabaddiStats(
    val playerName: String,
    val raidPoints: Int,
    val tacklePoints: Int,
    val totalPoints: Int
)

class BattingAdapter(private val battingList: List<BattingStats>) :
    RecyclerView.Adapter<BattingAdapter.BattingViewHolder>() {

    inner class BattingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val runs: TextView = view.findViewById(R.id.runs)
        val balls: TextView = view.findViewById(R.id.balls)
        val fours: TextView = view.findViewById(R.id.fours)
        val sixes: TextView = view.findViewById(R.id.sixes)
        val strikeRate: TextView = view.findViewById(R.id.strikeRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batting_score, parent, false)
        return BattingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BattingViewHolder, position: Int) {
        val player = battingList[position]
        holder.playerName.text = player.playerName
        holder.runs.text = player.runs.toString()
        holder.balls.text = player.balls.toString()
        holder.fours.text = player.fours.toString()
        holder.sixes.text = player.sixes.toString()
        holder.strikeRate.text = String.format("%.2f", player.strikeRate)
    }

    override fun getItemCount(): Int = battingList.size
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

    override fun getItemCount(): Int = bowlingList.size
}

class KabaddiAdapter(private val kabaddiList: List<KabaddiStats>) :
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
        val player = kabaddiList[position]
        holder.playerName.text = player.playerName
        holder.raidPoints.text = player.raidPoints.toString()
        holder.tacklePoints.text = player.tacklePoints.toString()
        holder.totalPoints.text = player.totalPoints.toString()
    }

    override fun getItemCount(): Int = kabaddiList.size
}*/
