package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.DatabaseAsteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private var _filterAsteroids = MutableLiveData(FilterAsteroid.ALL)

    private val _asteroidClicked = MutableLiveData<Asteroid>()
    val asteroidClicked: LiveData<Asteroid>
        get() = _asteroidClicked

    private val _apiError = MutableLiveData<Boolean>(false)
    val apiError: LiveData<Boolean>
        get() = _apiError

    @RequiresApi(Build.VERSION_CODES.O)
    var asteroidList = Transformations.switchMap(_filterAsteroids) {
        when(it) {
            FilterAsteroid.WEEK -> asteroidRepository.oneWeeksAsteroids
            FilterAsteroid.DAY -> asteroidRepository.todaysAsteroids
            else -> asteroidRepository.allAsteroids
        }
    }

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay


    init {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
                refreshPictureOfDay()
            } catch (e: Exception) {
                _apiError.postValue(true)
            }
        }
    }

    fun navigateToAsteroidDetail(asteroid: Asteroid) {
        _asteroidClicked.value = asteroid
    }

    fun doneNavigatingToAsteroidDetail() {
        _asteroidClicked.value = null
    }

    fun onChangeFilter(filter: FilterAsteroid) {
        _filterAsteroids.postValue(filter)
    }

    private suspend fun refreshPictureOfDay() {
        _pictureOfTheDay.value = asteroidRepository.getPictureOfDay()
    }
}