package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.domain.TodayAsteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidsDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAsteroidsList(asteroidList: List<Asteroid>)

    @Query(
        "SELECT * FROM asteroids WHERE closeApproachDate >= " +
                ":startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC"
    )
    fun getAsteroidsWithIntervalDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate ASC")
    fun getAllAsteroidsList(): Flow<List<Asteroid>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTodayAsteroid(todayAsteroid: TodayAsteroid)

    @Query("SELECT * FROM toady_asteroid")
    fun getTodayASteroid(): Flow<TodayAsteroid>

    @Query("DELETE FROM asteroids WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int

    @Query("DELETE FROM toady_asteroid WHERE date < :today")
    fun deletePreviousSingleDayAsteroid(today: String): Int

}