package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.END_DATE
import com.udacity.asteroidradar.Constants.MY_API_KEY
import com.udacity.asteroidradar.Constants.START_DATE
import com.udacity.asteroidradar.domain.TodayAsteroid
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("neo/rest/v1/feed")
    fun getAsteroidAsync(
        @Query(START_DATE) startDate: String,
        @Query(END_DATE) endDate: String,
        @Query(API_KEY) apiKey: String = MY_API_KEY,
    ): Call<String>

    @GET("planetary/apod")
    fun getTodayAsteroid(
        @Query(API_KEY) apiKey: String = MY_API_KEY,
    ): Call<TodayAsteroid>


}



