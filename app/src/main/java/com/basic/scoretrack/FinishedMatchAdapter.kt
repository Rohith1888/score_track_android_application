package com.basic.scoretrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FinishedMatchAdapter(private val matchList: List<FinishedMatch>) :
    RecyclerView.Adapter<FinishedMatchAdapter.MatchViewHolder>() {  // Fixed incorrect reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_item_finished, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matchList[position]

        holder.stadium.text = match.stadium
        holder.date.text = match.date
        holder.team1.text = match.team1
        holder.team1Score.text = match.team1Score
        holder.team2.text = match.team2
        holder.team2Score.text = match.team2Score
        holder.result.text = match.result


            holder.team1Logo.setImageResource(match.team1Logo)


        // Load Team 2 Logo (Drawable or URL)
            holder.team2Logo.setImageResource(match.team2Logo)

    }

    override fun getItemCount() = matchList.size

    class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stadium: TextView = itemView.findViewById(R.id.stadiumText)
        val date: TextView = itemView.findViewById(R.id.matchDate)
        val team1: TextView = itemView.findViewById(R.id.team1Name)
        val team1Logo: ImageView = itemView.findViewById(R.id.team1Logo)
        val team1Score: TextView = itemView.findViewById(R.id.team1Score)
        val team2: TextView = itemView.findViewById(R.id.team2Name)
        val team2Logo: ImageView = itemView.findViewById(R.id.team2Logo)
        val team2Score: TextView = itemView.findViewById(R.id.team2Score)
        val result: TextView = itemView.findViewById(R.id.matchResult)
    }
}
