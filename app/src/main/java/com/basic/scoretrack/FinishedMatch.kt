package com.basic.scoretrack

data class FinishedMatch(
    val stadium: String,
    val date: String,
    val team1: String,
    val team1Logo: Int,
    val team1Score: String,
    val team2: String,
    val team2Logo: Int,
    val team2Score: String,
    val result: String
)
