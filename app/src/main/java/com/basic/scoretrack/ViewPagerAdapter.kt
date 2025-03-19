package com.basic.scoretrack
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment, private val team1Players: List<Player>, private val team2Players: List<Player>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) PlayerListFragment(team1Players) else PlayerListFragment(team2Players)
    }
}
