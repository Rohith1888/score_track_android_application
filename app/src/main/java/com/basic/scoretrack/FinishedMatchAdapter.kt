package com.basic.scoretrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FinishedMatchAdapter(
    private val matches: List<FinishedMatch>,
    private val onItemClick: (FinishedMatch) -> Unit // Click listener
) : RecyclerView.Adapter<FinishedMatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stadiumText: TextView = view.findViewById(R.id.stadiumText)
        val matchDate: TextView = view.findViewById(R.id.matchDate)
        val team1Logo: ImageView = view.findViewById(R.id.team1Logo)
        val team1Name: TextView = view.findViewById(R.id.team1Name)
        val team1Score: TextView = view.findViewById(R.id.team1Score)
        val team2Logo: ImageView = view.findViewById(R.id.team2Logo)
        val team2Name: TextView = view.findViewById(R.id.team2Name)
        val team2Score: TextView = view.findViewById(R.id.team2Score)
        val matchResult: TextView = view.findViewById(R.id.matchResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_item_finished, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        holder.stadiumText.text = match.stadium
        holder.matchDate.text = match.date
        holder.team1Logo.setImageResource(match.team1Logo)
        holder.team1Name.text = match.team1
        holder.team1Score.text = match.team1Score
        holder.team2Logo.setImageResource(match.team2Logo)
        holder.team2Name.text = match.team2
        holder.team2Score.text = match.team2Score
        holder.matchResult.text = match.result

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(match)
        }
    }

    override fun getItemCount() = matches.size
}
