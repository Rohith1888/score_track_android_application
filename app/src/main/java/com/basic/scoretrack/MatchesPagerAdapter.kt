package com.basic.scoretrack

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MatchesPagerAdapter(fragment: Fragment, private val sportType: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UpcomingMatchesFragment.newInstance(sportType)
            1 -> LiveMatchesFragment.newInstance(sportType)
            2 -> FinishedMatchesFragment.newInstance(sportType)
            else -> Fragment()
        }
    }
}
