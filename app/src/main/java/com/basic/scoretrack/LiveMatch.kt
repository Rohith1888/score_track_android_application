package com.basic.scoretrack

data class LiveMatch(
    val stadium: String,
    val date: String,
    val team1: String,
    val team1LogoRes: Int? = null, // Optional drawable resource // Optional URL for logo
    val team1Score: String, // Example: "145/3 (15.4)"
    val team2: String,
    val team2LogoRes: Int? = null, // Optional drawable resource // Optional URL for logo
    val team2Score: String, // Example: "Yet to bat"
    val matchDecision: String, // Example: "CSK chose to bat"
    val isLive: Boolean = true // Always true for live matches
)
