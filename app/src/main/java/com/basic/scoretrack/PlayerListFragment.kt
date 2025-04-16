package com.basic.scoretrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayerListFragment(private val players: List<PlayerResponseUpcoming>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_player_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = view.findViewById(R.id.loadingIndicator)

        // Simulate short loading (you can remove the delay if unnecessary)
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.postDelayed({
            recyclerView.adapter = PlayerListAdapter(players)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }, 500) // Optional short delay for effect

        return view
    }
}
