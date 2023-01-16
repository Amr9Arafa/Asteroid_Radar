package com.udacity.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.TodayAsteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(
    val database: AsteroidDatabase,
    application: Application
) : AndroidViewModel(application) {
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private var _todayAsteroid = MutableLiveData<TodayAsteroid>()
    val todayAsteroid: LiveData<TodayAsteroid>
        get() = _todayAsteroid

    init {
        _todayAsteroid.value = TodayAsteroid("", "", "")
        getAstroidList()
        getTodayAsteroid()
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshData()
                asteroidsRepository.refreshTodayASteroid()

            } catch (e: Exception) {
                Toast.makeText(application, "Failure: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getTodayAsteroid() {
        viewModelScope.launch {
            database.databaseDao.getTodayASteroid().collect {

                if (it != null) {
                    _todayAsteroid.value = it
                }


            }


        }
    }

    private fun getAstroidList() {
        viewModelScope.launch {
            database.databaseDao.getAsteroidsWithIntervalDate(
                getNextSevenDaysFormattedDates
                    ().get(0),
                getNextSevenDaysFormattedDates().get(6)
            ).collect {
                _asteroidList.value = it

            }


        }
    }

    fun onItemMenuWeekAsteroidsClicked() {
        viewModelScope.launch {
            database.databaseDao.getAsteroidsWithIntervalDate(
                getNextSevenDaysFormattedDates
                    ().get(0),
                getNextSevenDaysFormattedDates().get(6)
            ).collect {
                _asteroidList.value = it

            }


        }
    }

    fun onItemMenuTodayAsteroidsClicked() {
        viewModelScope.launch {
            database.databaseDao.getAsteroidsWithIntervalDate(
                getToday(),
                getToday()
            ).collect {
                _asteroidList.value = it

            }


        }
    }

    fun onItemMenuSavedAsteroidsClicked() {
        viewModelScope.launch {
            database.databaseDao.getAllAsteroidsList().collect {
                _asteroidList.value = it
            }


        }
    }

}