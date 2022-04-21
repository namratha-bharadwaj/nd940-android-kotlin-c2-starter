package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.models.DatabaseAsteroid

@Dao
interface AsteroidDao {

    //Get all asteroids
    @Query("select * from asteroids_table")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    //Get today's asteroids
    @Query("select * from asteroids_table where closeApproachDate = :startDate order by closeApproachDate desc")
    fun getTodaysAsteroids(startDate: String): LiveData<List<DatabaseAsteroid>>

    //Get asteroids for a certain duration
    @Query("select * from asteroids_table where closeApproachDate between :startDate and :endDate order by closeApproachDate desc")
    fun getAsteroidsForAGivenDuration(startDate: String, endDate: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

}