package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.asDatabaseModel
import com.udacity.asteroidradar.models.asDomainModel
import com.udacity.asteroidradar.utils.DayUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AsteroidRepository(private val database: AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now().let { startDate ->
        startDate.format(DateTimeFormatter.ISO_DATE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().plusDays(7).let { endDate ->
        endDate.format(DateTimeFormatter.ISO_DATE)
    }

    val allAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    @SuppressLint("NewApi")
    val todaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodaysAsteroids(startDate)) {
            it.asDomainModel()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    val oneWeeksAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroidsForAGivenDuration(startDate, endDate)) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = AsteroidApiService.Network.retrofitService.getAsteroids(Constants.API_KEY)
                val parsedResult = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDao.insertAll(*parsedResult.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getPictureOfDay(): PictureOfDay? {
        var pictureOfDay: PictureOfDay? = null
        withContext(Dispatchers.IO) {
            pictureOfDay = AsteroidApiService.Network.retrofitService.getPictureOfDay(Constants.API_KEY)
        }
        return pictureOfDay
    }
}
