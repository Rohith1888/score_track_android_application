package com.basic.scoretrack

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/user/register")
    fun registerUser(@Body user: User): Call<User>

    @POST("/user/login")
    fun loginUser(@Body user: Loginuser): Call<LoginResponse>
    @GET("/upcoming/matches")
    fun getUpcomingMatches(): Call<List<Cricket_UpComing>>
    @GET("upcoming/match/{id}")
    suspend fun getMatchDetails(@Path("id") matchId: Int): MatchDetailsResponse

}
data class LoginResponse(
    val fullName: String,
    val email: String
)

data class Loginuser(
    val email: String,
    val password: String
)
data class PlayerResponseUpcoming(
    val id: Int,
    val name: String,
    val type: String
)

data class MatchDetailsResponse(
    val id: Int,
    val team1: String,
    val team2: String,
    val stadium: String,
    val date: String,
    val time: String,
    val sportType: String,
    val team1Logo: String,
    val team2Logo: String,
    val team1Players: List<PlayerResponseUpcoming>,
    val team2Players: List<PlayerResponseUpcoming>
)

