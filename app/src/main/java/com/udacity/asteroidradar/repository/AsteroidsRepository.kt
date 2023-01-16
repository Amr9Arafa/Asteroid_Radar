package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.RetrofitBuilder
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidsRepository(private val database: AsteroidDatabase) {


    suspend fun refreshData() {
        withContext(Dispatchers.IO) {

            val response = RetrofitBuilder.retrofitService.getAsteroidAsync(
                getNextSevenDaysFormattedDates
                    ().get(0),
                getNextSevenDaysFormattedDates().get(6)
            ).await()
            var asteroidsList = parseAsteroidsJsonResult(JSONObject(response))
            database.databaseDao.insertAsteroidsList(asteroidsList)


        }
    }


    suspend fun refreshTodayASteroid() {
        withContext(Dispatchers.IO) {

            val response = RetrofitBuilder.retrofitService.getTodayAsteroid().await()
            database.databaseDao.insertTodayAsteroid(response)


        }
    }

    suspend fun deletePreviousAsteroidsList() {
        withContext(Dispatchers.IO) {
            database.databaseDao.deletePreviousDayAsteroids(getToday())
        }
    }

    suspend fun deleteExpiredTodayAsteroid() {
        withContext(Dispatchers.IO) {
            database.databaseDao.deletePreviousSingleDayAsteroid(getToday())
        }
    }
}