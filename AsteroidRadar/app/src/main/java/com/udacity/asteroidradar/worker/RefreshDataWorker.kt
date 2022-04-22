package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class RefreshDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORKER_NAME = "RefreshAsteroidsWorker"
    }
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidRespository = AsteroidRepository(database)

        return try {
            asteroidRespository.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

}