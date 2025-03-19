package com.basic.scoretrack

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var sliderAdapter: ImageSliderAdapter
    private val handler = Handler(Looper.getMainLooper())

    private val originalImages = listOf(
        R.drawable.ipl_banner,
        R.drawable.pkl_banner
    )

    // Add the first image again at the end to create an illusion of infinite scrolling
    private val images = originalImages + originalImages.first()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_bar).visibility = View.VISIBLE
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize ViewPager2
        viewPager2 = view.findViewById(R.id.ipl_banner_slider)
        sliderAdapter = ImageSliderAdapter(images)
        viewPager2.adapter = sliderAdapter

        // Auto-slide every 3 seconds
        autoSlideImages()

        // Handle smooth infinite scrolling
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == images.size - 1) {
                    // Reset to the first image without animation
                    handler.postDelayed({
                        viewPager2.setCurrentItem(0, false)
                    }, 500)
                }
            }
        })

        return view
    }

    private fun autoSlideImages() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val nextItem = viewPager2.currentItem + 1
                viewPager2.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }, 3000)
    }
}
