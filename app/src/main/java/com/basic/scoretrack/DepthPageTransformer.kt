package com.basic.scoretrack

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        when {
            position < -1 -> view.alpha = 0f // Off-screen left
            position <= 0 -> { // Left to center transition
                view.alpha = 1f
                view.translationX = 0f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            position <= 1 -> { // Center to right transition
                view.alpha = 1 - position
                view.translationX = -position * view.width
                val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            else -> view.alpha = 0f // Off-screen right
        }
    }
}
