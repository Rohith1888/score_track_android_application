package com.basic.scoretrack

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/signup")
    fun registerUser(@Body user: User): Call<User>


}