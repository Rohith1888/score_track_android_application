package com.basic.scoretrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

class MatchAdapter(
    private val matches: List<Cricket_UpComing>,
    private val onItemClick: (Cricket_UpComing) -> Unit
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stadiumText: TextView = view.findViewById(R.id.stadiumText)
        val matchTime: TextView = view.findViewById(R.id.matchTime)
        val team1Logo: ImageView = view.findViewById(R.id.team1Logo)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team2Logo: ImageView = view.findViewById(R.id.team2Logo)
        val team2Name: TextView = view.findViewById(R.id.team2Name)
        val matchDate: TextView = view.findViewById(R.id.matchDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_item, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        // Displaying dynamic match data
        holder.stadiumText.text = match.stadium
        holder.matchDate.text = match.date
        holder.matchTime.text = "Starts at: ${match.time}"
        holder.team1Name.text = match.team1
        holder.team2Name.text = match.team2

        // Use Glide to load logos from URLs or resource names
        Glide.with(holder.itemView.context)
            .load(match.team1Logo)  // Assuming team1Logo is a URL or local path to the image
            .into(holder.team1Logo)

        Glide.with(holder.itemView.context)
            .load(match.team2Logo)  // Assuming team2Logo is a URL or local path to the image
            .into(holder.team2Logo)

        // Handle item click to pass data for detailed view
        holder.itemView.setOnClickListener {
            onItemClick(match)
        }
    }

    override fun getItemCount() = matches.size
}

data class Cricket_UpComing(
    val  id: Int,
    val stadium: String,
    val date: String,
    val time: String,
    val team1: String,
    val team1Logo: String,  // Updated to accept a String (URL or image resource name)
    val team2: String,
    val team2Logo: String   // Updated to accept a String (URL or image resource name)
)
