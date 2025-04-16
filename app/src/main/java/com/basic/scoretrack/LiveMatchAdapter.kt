package com.basic.scoretrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LiveMatchAdapter(
    private val liveMatchList: MutableList<LiveCricketMatch> = mutableListOf(),  // Or use a common interface/base class
    private val onItemClick: (LiveCricketMatch) -> Unit
) : RecyclerView.Adapter<LiveMatchAdapter.LiveMatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveMatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_item_live, parent, false)
        return LiveMatchViewHolder(view)
    }
    fun updateData(newList: List<LiveCricketMatch>) {
        (liveMatchList as MutableList).clear()
        liveMatchList.addAll(newList)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: LiveMatchViewHolder, position: Int) {
        val match = liveMatchList[position]

        holder.stadium.text = match.stadium
        holder.date.text = match.date
        holder.matchDecision.text = match.matchDecision
        holder.team1.text = match.team1
        holder.team1Score.text = match.team1Score
        holder.team2.text = match.team2
        holder.team2Score.text = match.team2Score

        // Load team logos using Glide (or any other image loader)
        Glide.with(holder.itemView.context)
            .load(match.team1Logo)  // Assuming team1Logo is a URL or local path
            .into(holder.team1Logo)

        Glide.with(holder.itemView.context)
            .load(match.team2Logo)  // Assuming team1Logo is a URL or local path
            .into(holder.team2Logo)

        holder.liveIndicator.visibility = if (match.live) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            onItemClick(match)
        }
    }


    override fun getItemCount() = liveMatchList.size

    class LiveMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stadium: TextView = itemView.findViewById(R.id.stadiumText)
        val date: TextView = itemView.findViewById(R.id.matchDate)
        val matchDecision: TextView = itemView.findViewById(R.id.matchDecision)
        val team1: TextView = itemView.findViewById(R.id.team1Name)
        val team1Logo: ImageView = itemView.findViewById(R.id.team1Logo)
        val team1Score: TextView = itemView.findViewById(R.id.team1Score)
        val team2: TextView = itemView.findViewById(R.id.team2Name)
        val team2Logo: ImageView = itemView.findViewById(R.id.team2Logo)
        val team2Score: TextView = itemView.findViewById(R.id.team2Score)
        val liveIndicator: TextView = itemView.findViewById(R.id.liveIndicator)
    }
}
